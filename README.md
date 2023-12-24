# Shrike

这是一个试验项目试图剥离出来的一部分，用于实现某种数据接口规范，目前处于 Snapshot 状态且不是一个完整的项目。

## 概念

- 服务：接受数据获取或提交请求的应用
- 开发商应用：开发商在服务处注册的实例，每个开发商应用有自己的 `AppKey` 和 `AppSecret`

## 项目构成

- `cc.ddrpa.shrike:shrike-core`：核心包，提供类、枚举、异常、接口定义，随机信息生成工具类
- `cc.ddrpa.shrike:shrike-signature`：签名计算包，提供签名计算相关的类和方法
- `cc.ddrpa.shrike:shrike-mybatis-plus`：提供基于 Mybatis Plus 的持久化实现
- `cc.ddrpa.shrike:shrike-spring-boot-starter`：提供 Spring Boot 自动配置类
- `cc.ddrpa.shrike:shrike-spring-boot-showcase`：Spring Boot 集成示例

## 怎样使用

### 接口请求方可以使用 shrike 计算签名

在项目中添加 `cc.ddrpa.shrike:shrike-signature` 依赖，使用 `cc.ddrpa.shrike.signature.Habitat` 中提供的方法计算接口签名中的各个要素，如创建签名密钥
SignKey，计算签名等。

``` java
Pair<byte[], BigInteger> signKeyAndNoncePair = Habitat.calculateSignatureKey(appSecret, null);
var signKey = signKeyAndNoncePair.getLeft();
var nonce = signKeyAndNoncePair.getRight();
var signature = BaseEncoding.base16().lowerCase().encode(
  Habitat.calculateSignature(signKey, endpoint, timestamp, payload));
```

### 作为接口提供方

接口提供方可以使用 shrike 管理 AppKey 和 AppSecret，也可以使用一些方法验证签名。

在 Spring Boot 项目中添加 `cc.ddrpa.shrike:shrike-spring-boot-starter` 依赖，
在 MySQL 数据库上执行 `db/mysql/schema.sql` 脚本创建对应的数据库。

如果对数据存储有不同的见解，也可以只添加 `cc.ddrpa.shrike:shrike-core` 依赖，然后自己实现 `cc.ddrpa.shrike.core.ShrikeVendorService`
接口。

## 注意事项

许多公开方法，尤其是密码学相关的方法，其返回值类型一般为 `byte[]`。Jackson 等序列化工具可能默认会将其转换为 Base64 编码的字符串。虽然本实现几乎不考虑限制数据传输时的编码方法，还是推荐统一使用十六进制小写字母方式编码，一般使用 `cc.ddrpa.shrike:shrike-core` 依赖的 `com.google.guava:guava` 提供的 `BaseEncoding.base16().lowerCase()` 方法即可。

## 路线图

- AccessToken 机制
- 访问控制包
  - 提供 IP，Nonce 时间戳之类的访问控制实现类（拦截器）
    - 提供细化的异常类
