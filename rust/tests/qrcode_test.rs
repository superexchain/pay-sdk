mod common;
use common::client;

use rust_decimal::Decimal;
use novax_sdk::pay::qrcode::DynamicQrPayCreateRequest;

#[test]
fn test_dynamic_qr_pay_create() {
    let ts = std::time::SystemTime::now()
        .duration_since(std::time::UNIX_EPOCH)
        .unwrap()
        .as_millis();

    let resp = client()
        .execute(&DynamicQrPayCreateRequest {
            receipt_order_id: ts.to_string(),
            company_user_id:  88_888_896,
            currency:         "usdt".into(),
            amount:           Decimal::ONE,
            comment:          Some("test".into()),
        })
        .expect("DynamicQrPayCreateRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
    // server returns the order id in msg, not data
    assert!(resp.msg.as_deref().map(|m| !m.is_empty()).unwrap_or(false), "msg is empty: {resp:?}");
}
