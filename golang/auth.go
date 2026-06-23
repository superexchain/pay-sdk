package novax

// AccessKeyCredentials holds an access-key / access-secret pair.
// The server resolves AccessKey to an ApiKey record and injects
// user_id server-side — the SDK never sends it.
type AccessKeyCredentials struct {
	AccessKey    string
	AccessSecret string
}
