use rust_decimal::Decimal;
use serde::Deserialize;

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct PayProtocolsResp {
    pub currency:               Option<String>,
    pub protocol:               Option<String>,
    pub currency_type:          Option<i32>,
    pub chain_name:             Option<String>,
    pub smart_contract_address: Option<String>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct PayOrderResp {
    pub short_name:          Option<String>,
    pub logo:                Option<String>,
    pub currency:            Option<String>,
    pub currency_number:     Option<Decimal>,
    pub pay_currency_number: Option<Decimal>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct PayOrderAddressResp {
    pub protocol:        Option<String>,
    pub currency:        Option<String>,
    pub receipt_address: Option<String>,
    pub end_time:        Option<i64>,
    pub status:          Option<i32>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct PayOrderAddressFixedResp {
    pub protocol:        Option<String>,
    pub receipt_address: Option<String>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ReceiptHashOrderResp {
    pub id:                     Option<i64>,
    pub user_id:                Option<i64>,
    pub user_currency_order_id: Option<i64>,
    pub company_order_id:       Option<String>,
    pub protocol:               Option<String>,
    pub currency:               Option<String>,
    pub from_address:           Option<String>,
    pub to_address:             Option<String>,
    pub tx_id:                  Option<String>,
    pub amount:                 Option<Decimal>,
    pub status:                 Option<i32>,
    pub success_time:           Option<i64>,
    pub create_time:            Option<i64>,
    #[serde(rename = "type")]
    pub order_type:             Option<i32>,
    pub fee:                    Option<Decimal>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ReceiptOrderResp {
    pub receipt_order_id:       Option<String>,
    pub currency:               Option<String>,
    pub protocol:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub currency_number:        Option<Decimal>,
    pub accept_currency_number: Option<Decimal>,
    pub counts:                 Option<i64>,
    pub ok_time:                Option<i64>,
    pub order_status:           Option<i32>,
    pub status:                 Option<i32>,
    pub end_time:               Option<i64>,
    pub lose_time:              Option<i64>,
    #[serde(default)]
    pub hash_orders:            Vec<ReceiptHashOrderResp>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ReceiptOrderAddressResp {
    pub receipt_order_id:       Option<String>,
    pub currency:               Option<String>,
    pub protocol:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub currency_number:        Option<Decimal>,
    pub accept_currency_number: Option<Decimal>,
    pub counts:                 Option<i64>,
    pub ok_time:                Option<i64>,
    pub order_status:           Option<i32>,
    pub status:                 Option<i32>,
    pub end_time:               Option<i64>,
    pub lose_time:              Option<i64>,
    #[serde(default)]
    pub hash_orders:            Vec<ReceiptHashOrderResp>,
    pub address:                Option<String>,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct WithdrawOrderResp {
    pub withdraw_order_id:      Option<String>,
    #[serde(rename = "type")]
    pub order_type:             Option<i32>,
    pub currency:               Option<String>,
    pub protocol:               Option<String>,
    pub smart_contract_address: Option<String>,
    pub address:                Option<String>,
    pub currency_number:        Option<Decimal>,
    pub status:                 Option<i32>,
    #[serde(default)]
    pub hash_orders:            Vec<ReceiptHashOrderResp>,
}
