# promotion checkout engine

### A promotion checkout engine is a checkout processor where given promotion can be applied while checkout and avail the offer.

There are many types of promotions. Currently promotion checkout engine supporting below type promotion offer.
```aidl
1.  Mulitpler Promotion -   multiplier
    e.x:    buy 'n' items of a SKU for a fixed price (3 A's for 130)
2.  Adder promotion     -   adder
    e.x:-   but SK1 & SK2 for a fixed price (C + D = 30)
```

User have option to choose promotion type while checkout. User can choose only promotion at a time or can select all a promotion at once only.
```aidl
Promotion choose option:-
1.  multiplier  -   user will avail only multiplier type promotion while checkout SKU
2.  adder       -   user will avail only adder type promotion while checkout SKU
3.  all         -   user will avail all multiplier & adder type promotion while checkout SKU
4.  none        -   user will not avail any promotion while checkout SKU

```

how to build and run test cases
```$xslt
./gradlew clean build
This will clea and build the jar and run the default unit test cases.
```
Check report for default test cases
```aidl
report path :- /build/reports/tests/test/classes/com.promotion.engine.PromotionEngineApplicationTests.html
```

how to run manually
```
1.  Import the promotion-engine repo in any IDE.
2.  Go to the test directory. Open the PromotionEngineTestUtils class.
    Use any of the created input sku unit file or create your own based upon same format.
    test files directory :- src/test/resources/input_files
3.  Select any of the test cases and use custom created sku_unit.json file and run the test case.
```

how to start sprint boot application through jar
```$xslt
java -Xms6G -Xmx10G -cp promotion-engine-0.0.1-SNAPSHOT.jar -Dloader.main=com.promotion.engine.PromotionEngineApplication org.springframework.boot.loader.PropertiesLauncher
or
java -jar promotion-engine-0.0.1-SNAPSHOT.jar
```
