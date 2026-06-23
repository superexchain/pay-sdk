use rust_decimal::Decimal;
use crate::request::{ApiRequest, HttpMethod};
use super::model::WithdrawOrderResp;

pub struct WithdrawOrdersRequest {
    pub withdraw_order_ids: Option<String>,
    pub status:             Option<i32>,
}

impl ApiRequest for WithdrawOrdersRequest {
    type Response = Vec<WithdrawOrderResp>;
    fn method(&self) -> HttpMethod { HttpMethod::Get }
    fn path(&self)   -> &str       { "/pay/v3/withdraw/orders" }
    fn query_params(&self) -> Vec<(String, String)> {
        let mut p = vec![];
        if let Some(v) = &self.withdraw_order_ids { p.push(("withdrawOrderIds".into(), v.clone())); }
        if let Some(v) = self.status              { p.push(("status".into(), v.to_string())); }
        p
    }
}

pub struct WithdrawAddOrderRequest {
    pub withdraw_order_id:      Option<String>,
    pub order_type:             Option<i32>,
    pub currency:               Option<String>,
    pub protocol:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub address:                Option<String>,
    pub currency_number:        Option<Decimal>,
    pub call_back_url:          Option<String>,
}

impl ApiRequest for WithdrawAddOrderRequest {
    type Response = WithdrawOrderResp;
    fn method(&self) -> HttpMethod { HttpMethod::Post }
    fn path(&self)   -> &str       { "/pay/v3/withdraw/order/add" }
    fn body(&self) -> Option<serde_json::Value> {
        Some(serde_json::json!({
            "withdrawOrderId":      self.withdraw_order_id,
            "type":                 self.order_type,
            "currency":             self.currency,
            "protocol":             self.protocol,
            "smartContractAddress": self.smart_contract_address,
            "address":              self.address,
            "currencyNumber":       self.currency_number.map(|d| d.to_string()),
            "callBackUrl":          self.call_back_url,
        }))
    }
}
