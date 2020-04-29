%====================================================================================
% bls_led description   
%====================================================================================
context(ctxblsled, "localhost",  "TCP", "8075").
 qactor( led, ctxblsled, "it.unibo.led.Led").
  qactor( manager, ctxblsled, "it.unibo.manager.Manager").
msglogging.
