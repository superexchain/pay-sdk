mod common;
use common::client;

use rust_decimal::Decimal;
use novax_sdk::pay::receipt::*;

#[test]
fn test_receipt_protocols_dynamic() {
    let resp = client()
        .execute(&ReceiptProtocolsRequest { receipt_type: 1 })
        .expect("ReceiptProtocolsRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}

#[test]
fn test_receipt_protocols_fixed() {
    let resp = client()
        .execute(&ReceiptProtocolsRequest { receipt_type: 6 })
        .expect("ReceiptProtocolsRequest (fixed) failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}

#[test]
fn test_receipt_address() {
    let resp = client()
        .execute(&ReceiptAddressRequest {
            protocol:               Some("TRC20".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
        })
        .expect("ReceiptAddressRequest failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}

#[test]
fn test_receipt_orders() {
    let resp = client()
        .execute(&ReceiptOrdersRequest { receipt_order_ids: None, order_status: None, status: None })
        .expect("ReceiptOrdersRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}

#[test]
fn test_receipt_add_order() {
    let ts = std::time::SystemTime::now()
        .duration_since(std::time::UNIX_EPOCH)
        .unwrap()
        .as_millis();
    let resp = client()
        .execute(&ReceiptAddOrderRequest {
            protocol:               Some("TRC20".into()),
            currency:               Some("USDT".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
            receipt_order_id:       Some(ts.to_string()),
            currency_number:        Some(Decimal::ONE),
            call_back_url:          Some("http://tripartite-payment-ts.dev.svc.cluster.local/v3/public/test/call/back".into()),
        })
        .expect("ReceiptAddOrderRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}

#[test]
fn test_receipt_confirm() {
    let resp = client()
        .execute(&ReceiptConfirmRequest {
            protocol:               Some("TRC20".into()),
            currency:               Some("usdt".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
            receipt_order_id:       Some("1234".into()),
        })
        .expect("ReceiptConfirmRequest failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}
