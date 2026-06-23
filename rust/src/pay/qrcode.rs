use rust_decimal::Decimal;
use crate::request::{ApiRequest, HttpMethod};

pub struct DynamicQrPayCreateRequest {
    pub receipt_order_id: String,
    pub company_user_id:  i64,
    pub currency:         String,
    pub amount:           Decimal,
    pub comment:          Option<String>,
}

impl ApiRequest for DynamicQrPayCreateRequest {
    type Response = String;
    fn method(&self) -> HttpMethod { HttpMethod::Post }
    fn path(&self)   -> &str       { "/pay/v3/qr-code-pay/dynamic/create" }
    fn body(&self) -> Option<serde_json::Value> {
        Some(serde_json::json!({
            "receiptOrderId": self.receipt_order_id,
            "companyUserId":  self.company_user_id,
            "currency":       self.currency,
            "amount":         self.amount.to_string(),
            "comment":        self.comment,
        }))
    }
}
