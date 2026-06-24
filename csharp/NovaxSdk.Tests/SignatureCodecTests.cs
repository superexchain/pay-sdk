namespace NovaxSdk.Tests;

public class SignatureCodecTests
{
    [Fact]
    public void DataToSign_GetRequest_SortsQuery()
    {
        var url    = "https://api.novax.com/pay/v3/token?b=2&a=1&a=11";
        var result = SignatureCodec.DataToSign("GET", url, null, 1700000000000L);
        Assert.Equal("GET&a=1,11&b=2&timestamp=1700000000000", result);
    }

    [Fact]
    public void DataToSign_PostRequest_SortsBodyKeys()
    {
        var body   = """{"chain":"TRC20","amount":100,"address":"Txxx"}"""u8.ToArray();
        var result = SignatureCodec.DataToSign(
            "POST", "https://api.novax.com/pay/v3/withdraw/order/add",
            body, 1700000000000L);
        Assert.Equal(
            "POST&address=Txxx&amount=100&chain=TRC20&timestamp=1700000000000",
            result);
    }

    [Fact]
    public void HmacSha256Hex_KnownValue()
    {
        var sig = SignatureCodec.HmacSha256Hex("hello", "secret");
        Assert.Equal(64, sig.Length);
        Assert.Equal(
            "88aab3ede8d3adf94d26ab90d3bafd4a2083070c3bcce9c014ee04a443847c0b",
            sig);
    }
}
