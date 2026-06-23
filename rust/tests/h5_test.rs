mod common;
use common::client;

use rust_decimal::Decimal;
use novax_sdk::pay::h5::*;

fn token() -> String {
    let resp = client()
        .execute(&PayTokenRequest {
            receipt_order_id: "1234".into(),
            currency_number:  Decimal::ONE,
        })
        .expect("PayTokenRequest failed");
    assert_eq!(resp.code, Some(200), "token: {:?}", resp.msg);
    resp.data.expect("token data is None")
}

#[test]
fn test_pay_token() {
    let t = token();
    println!("token = {t}");
    assert!(!t.is_empty());
}

#[test]
fn test_h5_order() {
    let resp = client()
        .execute(&H5OrderRequest { token: Some(token()) })
        .expect("H5OrderRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}

#[test]
fn test_h5_protocols() {
    let resp = client()
        .execute(&H5ProtocolsRequest { token: Some(token()) })
        .expect("H5ProtocolsRequest failed");
    println!("{resp:?}");
    assert_eq!(resp.code, Some(200), "{:?}", resp.msg);
}

#[test]
fn test_h5_address() {
    let resp = client()
        .execute(&H5AddressRequest {
            protocol:               Some("TRC20".into()),
            currency:               Some("USDT".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
            token:                  Some(token()),
        })
        .expect("H5AddressRequest failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}

#[test]
fn test_h5_ok_time() {
    let resp = client()
        .execute(&H5OkTimeRequest {
            protocol:               Some("TRC20".into()),
            currency:               Some("USDT".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
            token:                  Some(token()),
        })
        .expect("H5OkTimeRequest failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}

#[test]
fn test_h5_confirm() {
    let resp = client()
        .execute(&H5ConfirmRequest {
            protocol:               Some("TRC20".into()),
            currency:               Some("USDT".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
            token:                  Some(token()),
        })
        .expect("H5ConfirmRequest failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}

#[test]
fn test_h5_order_status() {
    let resp = client()
        .execute(&H5OrderStatusRequest {
            protocol:               Some("TRC20".into()),
            currency:               Some("USDT".into()),
            smart_contract_address: Some("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into()),
            company_user_id:        Some("88888896".into()),
            token:                  Some(token()),
        })
        .expect("H5OrderStatusRequest failed");
    println!("{resp:?}");
    assert!(resp.code.is_some());
}
