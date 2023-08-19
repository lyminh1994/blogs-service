## GraalVM Native Support

This project has been configured to let you generate either a lightweight container or a native executable.
It is also possible to run your tests in a native image.

### Lightweight Container with Cloud Native Buildpacks

If you're already familiar with Spring Boot container images support, this is the easiest way to get started.
Docker should be installed and configured on your machine prior to creating the image.

To create the image, run the following goal:

```
./mvnw spring-boot:build-image -Pnative
```

Then, you can run the app like any other container:

```
docker run --rm -p 8080:8080 blogs-service:0.0.1-SNAPSHOT
```

### Executable with Native Build Tools

Use this option if you want to explore more options such as running your tests in a native image.
The GraalVM `native-image` compiler should be installed and configured on your machine.

NOTE: GraalVM 22.3+ is required.

To create the executable, run the following goal:

```
./mvnw native:compile -Pnative
```

Then, you can run the app as follows:

```
target/blogs-service
```

You can also run your existing tests suite in a native image.
This is an efficient way to validate the compatibility of your application.

To run your existing tests in a native image, run the following goal:

```
./mvnw test -PnativeTest
```
# Setup HashiCorp Vault for storing key
1. Install HashiCorp Vault via Homebrew
```shell
brew install vault
```
2. Start Vault in Dev Model
```shell
vault server -dev
```
3. Access the Vault UI
```shell
export VAULT_ADDR='http://127.0.0.1:8200'
```
Open your web browser and navigate to http://127.0.0.1:8200/ui/ to access the Vault UI.
4. Sign In to the Vault UI
In the Terminal, you will see field labeled "Root Token" In development mode, Vault generates a root token for you to use. To sign in, copy the root token from the terminal output and paste it into the "Root Token" input field in the Vault UI.
5. Create a Secret
```shell
vault kv put secret/my-key key=my-secret-key
```
Here, <code>secret/my-key</code> is the path where the secret will be stored, and <code>key</code> is the name of the key within the secret.
6. To access secrets stored in HashiCorp Vault from a Spring Boot application
## Add Dependencies
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>
```
## Configure Vault Properties
```properties
spring.cloud.vault.token=<your-vault-token>
spring.cloud.vault.scheme=http
spring.cloud.vault.host=localhost
spring.cloud.vault.port=8200
```
Replace <your-vault-token> with your actual Vault token.
Access Secrets in Properties
```properties
myapp.secret=${secret/my-secret/key}
```
In this example, the ${...} notation retrieves the secret value from Vault and assigns it to the myapp.secret property.
