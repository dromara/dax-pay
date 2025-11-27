package org.dromara.daxpay.sdk;

/**
 * 测试用常量
 * @author xxm
 * @since 2025/10/31
 */
public interface ApiTestConsent {

    /** 支付网关地址 */
    String PAY_URL = "http://127.0.0.1:19999";
    /** 商户私钥 */
    String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClTNVpzrAL9IgQ" +
            "Z6HeV8Ov05gV9DLvgEVOxmxneCyaMLeAZBMyxs8Uvudw4QhPHercHLhg0Slhnoif" +
            "XI7tKO+/nm2gxcatfQzzYy9AN7A2BWcJZB8Yu4bWayzK3X9lRt9kXMTyiLwvk4o7" +
            "9x/STFcyraIFWepyBEg272z34gGk8PH8YTjkM8TJ2nwYuR0Xvs/HdEm1TzCugywZ" +
            "N2cgr/6OLfg3+marx5pRvizBWOAnabSEylCgWAld9TC4HRuItMGp4zX50d4QLuTY" +
            "RvZhdkOoRh3HKNurO3GbRKZjCXzzmZwY8E09O30CmhFZHcIZF8TylxCJ64HRh2x1" +
            "KH35lJojAgMBAAECggEAOR28XCwLzoW3Ahwc5UvkFPwDAAr6EqF60UZkrLfsiXat" +
            "4VIzBAeIBD4WkH1hNp06ysWtu95p8w4pXQ9JX48WkFp4vOW5ybZ85BhwejsDyxbA" +
            "zJDo4c3iQHKV7p7sZx0/EVmwv7EZfUL4r9GrECpKsvsmEb1I8g6iuUCvoVNZiBkf" +
            "BMy7bkDXJDJ/KYuRqEkfPfgqOOicJlXgjyiwtV9RN9SKErvnHo3kyt8RrSbCEYh2" +
            "y787gtPYf2U1XH674SAxd2Ki0rQ3CqHRjBUQGix7l8G5Hcv+TBJITNIktj8aJfu3" +
            "CvMnslQU9HEjbU8dMclywRSrjW839rym5lf+ptU/aQKBgQDJ6Bt5gsJlysy8P7ag" +
            "JlmQS0D5GmTx3GELLim1Uhbwwt6gCsQJf2l+0CnuEOBfJPGRdAUi35Tv8gDTG+9X" +
            "ai0wunNki6AHIfCtvY+zDxdgFtut1eIyq1ASFbAkW1jQRlXshVILAcU5GNjv8wOp" +
            "V9KcgkMGIsqGUdGb5drsbKDUPwKBgQDRlgtjktHXxDFsdkxUXCObCu6LevgdqgMH" +
            "qZ4mfui/NkTAuLFaFPddR+cmJNPPsy6MrHj97rZsPSTpaP0vQfJykFDlkm0xfBwv" +
            "dp41jLhWDN+OxsSs0eyoXptl3XUArsfipQ84tUODp9h86DoPs2oS39LuZj0DncY7" +
            "yXq1jCOxHQKBgFj603DfcXCOyV+E7KTzgbEXmRCu0yHLr3DP7U2dWcLM/nOlivNs" +
            "lT9v2aqzAU6s51Dkwoa15dtA2aAvxXDOuA+re8MpzWKXUIwg6D1PP0v3huS7R65w" +
            "1R7DNBcxsphHBwLvVlLHevVIwAIvJMPykjyrI4KGvp4nXKrJx4s97DrdAoGBAJab" +
            "401HuWH7C6UskYdhuvh0b51t3ZS7knfULODu++REZD21u0THoka3H+UqO8eatI3E" +
            "dyHLg+3eNoNAvghStJ4dFPUUN0GDNWHqNKC4odK8Z35bWgPyysTnT3ZxIN4/u0Yk" +
            "ZP7US1L1r716yBZ2UHiFvTcx4xCRNV3LWFHUBeYFAoGBAJKdUemy9ZFhtox7vLOG" +
            "U9EPScsh7xcyDcR8dEk+Ixjh8T8TnYjFxg4URAk7/uTki8WF9xcp6YntA92W22+J" +
            "Eje42gDW9bDb8I7BDlMigVCkM2A8cAsr3sHDdfo4N/PISL0dHpt2izEKWb6HZriK" +
            "AxnJl5/NyDmL/YfSVksVOZud" +
            "-----END PRIVATE KEY-----";
    /** 平台公钥 */
    String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUzVac6wC/SIEGeh3lfD" +
            "r9OYFfQy74BFTsZsZ3gsmjC3gGQTMsbPFL7ncOEITx3q3By4YNEpYZ6In1yO7Sjv" +
            "v55toMXGrX0M82MvQDewNgVnCWQfGLuG1mssyt1/ZUbfZFzE8oi8L5OKO/cf0kxX" +
            "Mq2iBVnqcgRINu9s9+IBpPDx/GE45DPEydp8GLkdF77Px3RJtU8wroMsGTdnIK/+" +
            "ji34N/pmq8eaUb4swVjgJ2m0hMpQoFgJXfUwuB0biLTBqeM1+dHeEC7k2Eb2YXZD" +
            "qEYdxyjbqztxm0SmYwl885mcGPBNPTt9ApoRWR3CGRfE8pcQieuB0YdsdSh9+ZSa" +
            "IwIDAQAB" +
            "-----END PUBLIC KEY-----";
}
