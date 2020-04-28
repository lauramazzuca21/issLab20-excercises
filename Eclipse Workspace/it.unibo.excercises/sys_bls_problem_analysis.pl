%====================================================================================
% sys_bls_problem_analysis description   
%====================================================================================
context(ctxbutton, "localhost",  "TCP", "8010").
context(ctxled, "127.0.0.1",  "TCP", "8010").
 qactor( button, ctxbutton, "it.unibo.button.Button").
  qactor( led, ctxled, "it.unibo.led.Led").
  qactor( user, ctxbutton, "it.unibo.user.User").
