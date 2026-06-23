use std::collections::BTreeMap;

use hmac::{Hmac, Mac};
use sha2::Sha256;

use crate::error::NovaxError;
use crate::request::HttpMethod;

type HmacSha256 = Hmac<Sha256>;

/// Mirrors the server-side `SignatureUtil`.
///
/// `dataToSign = METHOD[&sorted-query][&sorted-body]&timestamp=<ms>`
pub struct SignatureCodec;

impl SignatureCodec {
    pub fn data_to_sign(
        method:       HttpMethod,
        url:          &str,
        body:         Option<&[u8]>,
        timestamp_ms: u64,
    ) -> Result<String, NovaxError> {
        let mut parts = vec![method.as_str().to_uppercase()];

        let sorted_query = Self::sorted_query(url);
        if !sorted_query.is_empty() {
            parts.push(sorted_query);
        }

        if let Some(b) = body {
            if !b.is_empty() {
                if let Ok(sorted) = Self::sorted_body(b) {
                    if !sorted.is_empty() {
                        parts.push(sorted);
                    }
                }
            }
        }

        parts.push(format!("timestamp={}", timestamp_ms));
        Ok(parts.join("&"))
    }

    pub fn hmac_sha256_hex(data: &str, secret: &str) -> Result<String, NovaxError> {
        let mut mac = HmacSha256::new_from_slice(secret.as_bytes())
            .map_err(|e| NovaxError::Signature(e.to_string()))?;
        mac.update(data.as_bytes());
        Ok(hex::encode(mac.finalize().into_bytes()))
    }

    fn sorted_query(url: &str) -> String {
        let query = match url.find('?') {
            Some(pos) => &url[pos + 1..],
            None      => return String::new(),
        };

        let mut grouped: BTreeMap<String, Vec<String>> = BTreeMap::new();
        for pair in query.split('&') {
            if pair.is_empty() { continue; }
            let (k, v) = match pair.find('=') {
                Some(pos) => (&pair[..pos], &pair[pos + 1..]),
                None      => (pair, ""),
            };
            let key   = urlencoding::decode(k).unwrap_or_default().into_owned();
            let value = urlencoding::decode(v).unwrap_or_default().into_owned();
            grouped.entry(key).or_default().push(value);
        }

        grouped
            .iter()
            .map(|(k, vs)| format!("{}={}", k, vs.join(",")))
            .collect::<Vec<_>>()
            .join("&")
    }

    fn sorted_body(body: &[u8]) -> Result<String, NovaxError> {
        let parsed: serde_json::Value = serde_json::from_slice(body)?;
        Ok(match &parsed {
            serde_json::Value::Array(arr) => arr
                .iter()
                .map(Self::sort_object)
                .collect::<Vec<_>>()
                .join("&"),
            _ => Self::sort_object(&parsed),
        })
    }

    fn sort_object(val: &serde_json::Value) -> String {
        let obj = match val.as_object() {
            Some(m) => m,
            None    => return val.to_string(),
        };
        let sorted: BTreeMap<&String, &serde_json::Value> = obj.iter().collect();
        sorted
            .iter()
            .map(|(k, v)| format!("{}={}", k, Self::render_value(v)))
            .collect::<Vec<_>>()
            .join("&")
    }

    fn render_value(val: &serde_json::Value) -> String {
        match val {
            serde_json::Value::Null      => String::new(),
            serde_json::Value::String(s) => s.clone(),
            serde_json::Value::Bool(b)   => b.to_string(),
            serde_json::Value::Number(n) => n.to_string(),
            _                            => val.to_string(),
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn get_sorted_query() {
        let url = "https://api.novax.com/pay/v3/token?b=2&a=1&a=11";
        let got = SignatureCodec::data_to_sign(HttpMethod::Get, url, None, 1700000000000).unwrap();
        assert_eq!(got, "GET&a=1,11&b=2&timestamp=1700000000000");
    }

    #[test]
    fn post_sorted_body() {
        let url  = "https://api.novax.com/pay/v3/withdraw/order/add";
        let body = br#"{"chain":"TRC20","amount":100,"address":"Txxx"}"#;
        let got  = SignatureCodec::data_to_sign(HttpMethod::Post, url, Some(body), 1700000000000).unwrap();
        assert_eq!(got, "POST&address=Txxx&amount=100&chain=TRC20&timestamp=1700000000000");
    }

    #[test]
    fn hmac_sha256_known_value() {
        let sig = SignatureCodec::hmac_sha256_hex("hello", "secret").unwrap();
        assert_eq!(sig.len(), 64);
        assert_eq!(sig, "88aab3ede8d3adf94d26ab90d3bafd4a2083070c3bcce9c014ee04a443847c0b");
    }
}
