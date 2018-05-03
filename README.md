# folx-it-quest

## restclient

To use restclient module:
 1. We need a Spring project
 2. We add restclient as dependency to this project
 3. We make sure that we have RestTemplate bean and ObjectMapper bean with possibility to use Kotlin classes
 4. We add to our application.yaml property: itquest.restclient.basePath with base address of rest service
 4. We use @Import annotation to import RestClientConfiguration class
 