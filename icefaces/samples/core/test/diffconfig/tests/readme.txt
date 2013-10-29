Testing basic example with Selenium IDE
----------------------

The basicICETestSuite depends on 2 different html scripts for Selenium.  The basicICEfaces2 script, merely opens the window of the application and, starting from a "Show" state of the view, will alternate back and forth between the 2 states.  The basicICE2.0REloadRetry script merely clicks on the reload button.  The test suite will then reload the first script which alternates between the 2 states.