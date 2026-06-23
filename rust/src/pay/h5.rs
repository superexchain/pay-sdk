use rust_decimal::Decimal;
use crate::request::{ApiRequest, HttpMethod};
use super::model::{PayOrderAddressResp, PayOrderResp, PayProtocolsResp};

pub struct PayTokenRequest {
    pub receipt_order_id: String,
    pub currency_number:  Decimal,
}

impl ApiRequest for PayTokenRequest {
    type Response = String;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/v3/token" }
    fn query_params(&self) -> Vec<(String, String)> {
        vec![
            ("receiptOrderId".into(), self.receipt_order_id.clone()),
            ("currencyNumber".into(), self.currency_number.to_string()),
        ]
    }
}

pub struct H5OrderRequest {
    pub token: Option<String>,
}

impl ApiRequest for H5OrderRequest {
    type Response = PayOrderResp;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/public/h5/order" }
    fn query_params(&self) -> Vec<(String, String)> {
        self.token.iter().map(|t| ("token".into(), t.clone())).collect()
    }
}

pub struct H5ProtocolsRequest {
    pub token: Option<String>,
}

impl ApiRequest for H5ProtocolsRequest {
    type Response = Vec<PayProtocolsResp>;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/public/h5/protocols" }
    fn query_params(&self) -> Vec<(String, String)> {
        self.token.iter().map(|t| ("token".into(), t.clone())).collect()
    }
}

pub struct H5AddressRequest {
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
    pub token:                  Option<String>,
}

impl ApiRequest for H5AddressRequest {
    type Response = PayOrderAddressResp;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/public/h5/address" }
    fn query_params(&self) -> Vec<(String, String)> {
        let mut p = vec![];
        if let Some(v) = &self.protocol               { p.push(("protocol".into(), v.clone())); }
        if let Some(v) = &self.currency               { p.push(("currency".into(), v.clone())); }
        if let Some(v) = &self.smart_contract_address { p.push(("smartContractAddress".into(), v.clone())); }
        if let Some(v) = &self.company_user_id        { p.push(("companyUserId".into(), v.clone())); }
        if let Some(v) = &self.token                  { p.push(("token".into(), v.clone())); }
        p
    }
}

pub struct H5OkTimeRequest {
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
    pub token:                  Option<String>,
}

impl ApiRequest for H5OkTimeRequest {
    type Response = bool;
    fn method(&self) -> HttpMethod { HttpMethod::Post }
    fn path(&self)   -> &str       { "/pay/public/h5/ok-time" }
    fn body(&self) -> Option<serde_json::Value> {
        Some(serde_json::json!({
            "protocol":             self.protocol,
            "currency":             self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId":        self.company_user_id,
            "token":                self.token,
        }))
    }
}

pub struct H5ConfirmRequest {
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
    pub token:                  Option<String>,
}

impl ApiRequest for H5ConfirmRequest {
    type Response = serde_json::Value;
    fn method(&self) -> HttpMethod { HttpMethod::Post }
    fn path(&self)   -> &str       { "/pay/public/h5/confirm" }
    fn body(&self) -> Option<serde_json::Value> {
        Some(serde_json::json!({
            "protocol":             self.protocol,
            "currency":             self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId":        self.company_user_id,
            "token":                self.token,
        }))
    }
}

pub struct H5OrderStatusRequest {
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
    pub token:                  Option<String>,
}

impl ApiRequest for H5OrderStatusRequest {
    type Response = PayOrderAddressResp;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/public/h5/order/pay/status" }
    fn query_params(&self) -> Vec<(String, String)> {
        let mut p = vec![];
        if let Some(v) = &self.protocol               { p.push(("protocol".into(), v.clone())); }
        if let Some(v) = &self.currency               { p.push(("currency".into(), v.clone())); }
        if let Some(v) = &self.smart_contract_address { p.push(("smartContractAddress".into(), v.clone())); }
        if let Some(v) = &self.company_user_id        { p.push(("companyUserId".into(), v.clone())); }
        if let Some(v) = &self.token                  { p.push(("token".into(), v.clone())); }
        p
    }
}
