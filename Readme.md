## Feign optional URI parameter example

This is an example application for reproducing the issue with Feign/Spring Cloud OpenFeign not
accepting null values for optional URI parameter.

Check out the test scenarios in `ServerApiClientTest` or launch the application and send a request
to `ClientController#createResourceViaClient` with some file:
```shell
curl --location 'http://127.0.0.1:8080/client/resources' \
--form 'file=@"/Users/user/file.txt"'
```
