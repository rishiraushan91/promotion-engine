# promotion checkout engine

### A promotion checkout engine is a checkout processor where given promotion can be applied while checkout and avail the offer.

There are many types of promotions. Currently promotion checkout engine supporting below type promotion offer.
```aidl
1.  Multiplier Promotion -   multiplier
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

Sample run output :-
```$xslt
02:47:16.254 [TestNG-test=Promotion Engine Tests-1] INFO com.promotion.engine.service.CheckoutEngineService - 
SKU Units: {A=SKUUnit(sku=A, unit=5, checkout=false), B=SKUUnit(sku=B, unit=5, checkout=false), C=SKUUnit(sku=C, unit=1, checkout=false)}
02:47:16.254 [TestNG-test=Promotion Engine Tests-1] INFO com.promotion.engine.service.CheckoutEngineService - 
SKU Prices: {A=50, B=30, C=20, D=15}
02:47:16.255 [TestNG-test=Promotion Engine Tests-1] INFO com.promotion.engine.service.CheckoutEngineService - 
Multiplier promotions: {A=MultiplierPromotion(sku=A, unit=3, price=130), B=MultiplierPromotion(sku=B, unit=2, price=45)}
******* Promotions ******
{
  "multiplier_sku": [
    {
      "unit": 3,
      "price": 130,
      "sku": "A"
    },
    {
      "unit": 2,
      "price": 45,
      "sku": "B"
    }
  ],
  "adder_sku": [
    {
      "price": 30,
      "sku": [
        "C",
        "D"
      ]
    },
    {
      "price": 20,
      "sku": [
        "E",
        "F"
      ]
    }
  ]
}

******* SKU Price ******
{"sku_price": [
  {
    "price": 50,
    "sku": "A"
  },
  {
    "price": 30,
    "sku": "B"
  },
  {
    "price": 20,
    "sku": "C"
  },
  {
    "price": 15,
    "sku": "D"
  }
]}

******* SKU Input ******
{"sku_unit": [
  {
    "unit": 5,
    "sku": "A"
  },
  {
    "unit": 5,
    "sku": "B"
  },
  {
    "unit": 1,
    "sku": "C"
  }
]}
***** Checkout Promotion Price ****
***** Total:	370 *****
```