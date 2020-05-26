%====================================================================================
% ledalone description   
%====================================================================================
context(ctxledalone, "localhost",  "TCP", "8080").
 qactor( led, ctxledalone, "it.unibo.led.Led").
msglogging.
