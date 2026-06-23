use rust_decimal::Decimal;
use crate::request::{ApiRequest, HttpMethod};
use super::model::{PayOrderAddressFixedResp, PayProtocolsResp, ReceiptOrderAddressResp, ReceiptOrderResp};

pub struct ReceiptProtocolsRequest {
    /// 1 = dynamic address, 6 = fixed address.
    pub receipt_type: i32,
}

impl Default for ReceiptProtocolsRequest {
    fn default() -> Self { Self { receipt_type: 1 } }
}

impl ApiRequest for ReceiptProtocolsRequest {
    type Response = Vec<PayProtocolsResp>;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/v3/protocols" }
    fn query_params(&self) -> Vec<(String, String)> {
        vec![("type".into(), self.receipt_type.to_string())]
    }
}

pub struct ReceiptAddressRequest {
    pub protocol:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
}

impl ApiRequest for ReceiptAddressRequest {
    type Response = PayOrderAddressFixedResp;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/v3/receipt/address" }
    fn query_params(&self) -> Vec<(String, String)> {
        let mut p = vec![];
        if let Some(v) = &self.protocol               { p.push(("protocol".into(), v.clone())); }
        if let Some(v) = &self.smart_contract_address { p.push(("smartContractAddress".into(), v.clone())); }
        if let Some(v) = &self.company_user_id        { p.push(("companyUserId".into(), v.clone())); }
        p
    }
}

pub struct ReceiptOrdersRequest {
    pub receipt_order_ids: Option<String>,
    pub order_status:      Option<i32>,
    pub status:            Option<i32>,
}

impl ApiRequest for ReceiptOrdersRequest {
    type Response = Vec<ReceiptOrderResp>;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/v3/receipt/orders" }
    fn query_params(&self) -> Vec<(String, String)> {
        let mut p = vec![];
        if let Some(v) = &self.receipt_order_ids { p.push(("receiptOrderIds".into(), v.clone())); }
        if let Some(v) = self.order_status        { p.push(("orderStatus".into(), v.to_string())); }
        if let Some(v) = self.status              { p.push(("status".into(), v.to_string())); }
        p
    }
}

pub struct ReceiptAddOrderRequest {
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
    pub receipt_order_id:       Option<String>,
    pub currency_number:        Option<Decimal>,
    pub call_back_url:          Option<String>,
}

impl ApiRequest for ReceiptAddOrderRequest {
    type Response = ReceiptOrderAddressResp;
    fn method(&self) -> HttpMethod { HttpMethod::Post }
    fn path(&self)   -> &str       { "/pay/v3/receipt/order/add" }
    fn body(&self) -> Option<serde_json::Value> {
        Some(serde_json::json!({
            "protocol":             self.protocol,
            "currency":             self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId":        self.company_user_id,
            "receiptOrderId":       self.receipt_order_id,
            "currencyNumber":       self.currency_number.map(|d| d.to_string()),
            "callBackUrl":          self.call_back_url,
        }))
    }
}

pub struct ReceiptConfirmRequest {
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub company_user_id:        Option<String>,
    pub receipt_order_id:       Option<String>,
}

impl ApiRequest for ReceiptConfirmRequest {
    type Response = serde_json::Value;
    fn method(&self) -> HttpMethod { HttpMethod::Post }
    fn path(&self)   -> &str       { "/pay/v3/receipt/order/confirm" }
    fn body(&self) -> Option<serde_json::Value> {
        Some(serde_json::json!({
            "protocol":             self.protocol,
            "currency":             self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId":        self.company_user_id,
            "receiptOrderId":       self.receipt_order_id,
        }))
    }
}
