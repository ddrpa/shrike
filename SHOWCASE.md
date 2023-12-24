# Spring Boot Based Showcase

本文中展示的接口用于演示，查看对应接口的源码了解如何使用 Shrike 完成任务。

## 开发商应用管理

开发商在服务处注册的实例，每个开发商应用有自己的`AppKey` 和 `AppSecret`。

```shell
curl -X POST http://localhost:8080/register-vendor | jq
```

- AppKey，随机生成的字符串
- AppSecret，随机生成的字符串

默认实现中，AppSecret 仅在应用生成时展示，之后不再展示，也不提供设定为指定值的方法。

```shell
{
  "appKey":"8396fca5686ecb1a2c95011bc4f51d18",
  "appSecret":"cds0eGBYce2C6LtCl8IUsj+mIY86mlmmAqnCZw7QVgg=",
  "description":"add at 2023-10-10T17:42:46.879157",
  "enabled":true
}   
```

获取所有启用中的应用信息：

```shell
curl http://localhost:8080/list-vendor | jq
```

```shell
[
  {
    "appKey": "8396fca5686ecb1a2c95011bc4f51d18",
    "appSecret": null,
    "description": "add at 2023-10-10T17:42:46.879157",
    "enabled": true
  },
  {
    "appKey": "c6865234191292ba645c02115b69c387",
    "appSecret": null,
    "description": "add at 2023-10-10T16:42:26.686372",
    "enabled": true
  }
]
```

AppSecret 不允许修改，只允许重置：

```shell
curl -X POST http://localhost:8080/reset-secret/7028942dc9a81b90 | jq
```

启用 / 停用某个应用，该功能未实现

```shell
curl http://localhost:8080/vendor/{enable-vendor | disable-vendor}/7028942dc9a81b90 | jq
```

## 开发商应用获取 Token

接受 AppKey 和 AppSecret 做身份验证，用以换取 AccessToken。由于要涉及过期等内容，shrike 暂未涉及 AccessToken 机制的实现。

```shell
curl -X POST http://localhost:8080/authenticate -H "Content-Type: application/json" -d '{"appKey":"7028942dc9a81b90","appSecret":"n5/Mun5eQcmyRTRELp3k8ugzAt+efxaa5W26xFCwZFk="}' | jq
```

## 计算请求签名

服务端使用接口请求方声称的 AppKey 和 Cnonce 计算请求签名

```shell
curl -X POST http://localhost:8080/validate-signature -H "Content-Type: application/json" -d '{"appKey":"7028942dc9a81b90","signature": "9c3dd5eb3b53a5c6f70adad1a391400d672f86104d985870ce9e0ee80ff51a07","nonce": "1080377950","timestamp": "1697005264"}' | jq
```
