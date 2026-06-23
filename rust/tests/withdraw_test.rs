mod common;
use common::client;

use rust_decimal::Decimal;
use novax_sdk::pay::withdraw::*;

#[test]
fn test_withdraw_orders() {
    let resp = client()
        .execute(&WithdrawOrdersRequest { withdraw_order_ids: Some("w_1234,1745564918818000".into()), status: None })
        .expect("WithdrawOrdersRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}

#[test]
fn test_withdraw_add_order() {
    let ts = std::time::SystemTime::now()
        .duration_since(std::time::UNIX_EPOCH)
        .unwrap()
        .as_millis();
    let resp = client()
        .execute(&WithdrawAddOrderRequest {
            withdraw_order_id:      Some(ts.to_string()),
            order_type:             Some(1),
            currency:               Some("USDT".into()),
            protocol:               Some("TRC20".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            address:                Some("TVKUpYxUV4LTdFZ24kNrvMm6phXx6vv7Zc".into()),
            currency_number:        Some(Decimal::ONE),
            call_back_url:          Some("https://example.com/callback".into()),
        })
        .expect("WithdrawAddOrderRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}
