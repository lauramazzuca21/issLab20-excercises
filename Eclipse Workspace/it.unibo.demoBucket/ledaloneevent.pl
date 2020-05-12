%====================================================================================
% ledaloneevent description   
%====================================================================================
context(ctxledalone, "localhost",  "TCP", "8080").
 qactor( led, ctxledalone, "it.unibo.led.Led").
  qactor( abutton, ctxledalone, "it.unibo.abutton.Abutton").
msglogging.
