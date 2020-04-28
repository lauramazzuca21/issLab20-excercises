%====================================================================================
% sys_bls_logical_architecture description   
%====================================================================================
context(ctxlogicbutton, "localhost",  "TCP", "8010").
context(ctxlogicled, "127.0.0.1",  "TCP", "8077").
 qactor( button, ctxlogicbutton, "it.unibo.button.Button").
  qactor( user, ctxlogicbutton, "it.unibo.user.User").
  qactor( led, ctxlogicled, "it.unibo.led.Led").
