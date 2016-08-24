(
Server.default = Server.local;
s.options.maxNodes_(4096);       //Maximize Memory
s.options.memSize_(16384);
 s.reboot;
);

 o = Server.local.options;
 o.maxNodes.postln;            //See if Maximization is Done
 o.memSize.postln;
s.recChannels.postln;

~buffs = SoundFile.collectIntoBuffers( "/Users/admin/STUDIO/SCfold/musicbox/*");         //Load Buffers
(
~specs = (
       rate1: ControlSpec(1, 2, \exp, 0.01, 0),
       rate2: ControlSpec(1, 3, \exp, 0.01, 0),
       rate3: ControlSpec(0.5, 1.5, \exp, 0.01, 0),
       rate4: ControlSpec(1, 1.5, \exp, 0.01, 0),
       rate5: ControlSpec(1, 1.5, \exp, 0.01, 0),
   attackall: ControlSpec(0.01, 0.09, \exp, 0.01, 0.01),
  sustainall: ControlSpec(0.1, 1.0, \exp, 0.1, 1.0),
  releaseall: ControlSpec(0.03, 0.5, \exp, 0.01, 0.5)
        );
  );

(
SynthDef(\grains, {|out= 0, bufnum, trigger= 1, startPos= 0.5, rate= 1, attack=
0.01, sustain= 0.6, release= 0.5, pan= 0, amp= 1, inter=2, phase=1|
        var play, env;
        env= Linen.kr(Impulse.kr(0), attack, sustain, release, doneAction:2);
        play= BufRd.ar(1, bufnum, LFSaw.ar(BufDur.ir(bufnum).reciprocal).range * BufFrames.ir(bufnum) * rate * BufRateScale.ir(bufnum));
     OffsetOut.ar( out,
     PanAz.ar( 2, play * env, pan * 2.0 / 2, amp, 2.0, 0.5), 0.8);
}).add;
);

//Synth(\grains);

(
~rate = 1;
~int1 = 1;
~int2 = 1;
~int3 = 100;
~int4 = 200;
~int5 = 100;
~duration = 10.25;
~duration1 = 0.25;
~duration2 = 0.20;
~duration3 = 0.20;
~duration4 = 0.001;
~amp1 = 1;
~amp2 =1;
~amp3 = 1;
~amp4 = 1;
~attack = 0.01;
~sustain = 1;
~release = 0.1;
~panMax = 0.5;
~loopDur = 0.5;
);
(
~gesture2 = true;
~gesture3 = true;
~gesture4 = true;
~gesturescore = true;
);

//s.stopRecording;

~p.play;
~p.stop;

~gesture2 = false;
~gesture3 = false;
~gesture4 = false;
~gesturescore = false;

(
~p = Pspawner({ |sp|
        f = sp;
~a = f.par(
Pbind(\instrument, \grains,
\amp, Prand([0.9, 1.0], inf) * Pfunc{~amp1},
\pan, Prand([0,1], inf),
\bufnum, ~buffs[6],
\rate,  Pgbrown(Pfunc{~int1}, Pfunc{~int2}, 2.0, inf) * Pfunc { ~specs[\rate1].map(~rate) },
\startPos, Pwhite(0.1, 1.0, inf) * Pfunc { ~specs[\attackall].map(~attack) }.poll,
\attack, Pbeta(0.01, 0.2, 5, 2 ) * Pfunc { ~specs[\attackall].map(~attack) },
\sustain, max(0.003, Pwhite(0.9, 1.0, inf) * Pfunc { ~specs[\sustainall].map(~sustain) }),
\release, Pgbrown(0.2, 2.0, 0.1) * Pfunc { ~specs [\releaseall].map (~release) },
\dur, Pgbrown (0.01, 0.1, Pfunc {~duration1}, inf )
).asStream);


   f.seq(Pbind(\type, Pif(Pfunc({ ~gesture2 }), \rest), \dur, 0.125));

 ~b = f.par(
PfadeIn(
Pbind(\instrument, \grains,
\amp,  Prand([(0.8..1.0)], inf) * Pfunc{~amp2},
\pan, Prand([0,1], inf),
\bufnum, ~buffs[3,5,7],
\rate,  Pgbrown (Pbeta(10, 20, 0.2, 0.2, inf), Pbeta(10, 20, 2.0, 2.0, inf), Pbeta(1.0, 0.2, 0.1, 0.1, inf), inf) * Pfunc { ~specs[\rate2].map(~rate) },
\startPos, Pwhite(0.1, 1.0, inf) * Pfunc { ~specs[\attackall].map(~attack) },
\attack, Pbeta(0.01, 0.2, 5, 2 ) * Pfunc { ~specs[\attackall].map(~attack) },
\sustain, max(0.003, Pwhite(0.7, 1.0, inf) * Pfunc { ~specs[\sustainall].map(~sustain) }),
\release, Pgbrown(0.1, 1.0, 0.125) * Pfunc { ~specs [\releaseall].map (~release) },
\dur, Pgbrown (0.01, 0.1, Pfunc {~duration2}, inf )
).asStream,
0.1, 0.1, 0.0001));

	f.seq(Pbind(\type, Pif(Pfunc({ ~gesture3 }), \rest), \dur, 0.125));

~c = f.par(
PfadeIn(
Pbind(\instrument, \grains,
\amp, Prand([(0.8..1.0)], inf) * Pfunc{~amp3},
\pan, Prand([0,1], inf),
\bufnum, ~buffs[5,6,7],
\rate, Ptuple([
Pseg(Pseq([10, 20], inf), 10),
	Pseg(Pseq([40, 60], inf), 10),
	Pseg(Pseq([80, 20], inf), 10)
], inf) * Pfunc { ~specs[\rate3].map(~rate) },
\release, Pgbrown(0.2, 2.0, 0.1) * Pfunc { ~specs [\releaseall].map (~release) } * Pfunc { ~expression },
\dur, Prand([0.1, 0.05, 0.03, 0.05, 0.07], inf)
).asStream,
120, 2, 0.01));

 f.seq(Pbind(\type, Pif(Pfunc({ ~gesture4 }), \rest), \dur, 0.125));

~d = f.par(
PfadeIn(
Pbind(\instrument, \grains,
\amp, Prand([(0.6..1.0)], inf) * Pfunc{~amp4},
\pan, Prand([0,1], inf),
\bufnum, ~buffs[5,2,3],
\rate, Pgbrown(-1.0, 1, 0.125, inf).next * Pfunc { ~specs[\rate4].map(~rate) } ,
\attack,   1.0, // Pwhite (0.01, 0.09,  inf ) * Pfunc { ~specs[\attackall].map(~attack) },
\sustain, max(0.003, Pwhite(0.6, 1.0, inf) * Pfunc { ~specs[\sustainall].map(~sustain) }),
\release,  Pgbrown(0.1, 0.2, 0.01) * Pfunc { ~release },
\dur,  Pseq([0.01, 0.1], inf)
).asStream, 0, 0, 0.01));

/*
f.seq(Pbind(\type, Pif(Pfunc({ ~gesturescore }), \rest), \dur, 0.125));
                          //Use A Score
       9.0.wait;
       f.suspend(~a);
       9.0.wait;
       f.suspend(~b);
       9.0.wait;
       f.suspend(~c);
       9.0.wait;
       f.suspend(~d);
*/
});

OSCFunc({|msg|  ~rate = msg[1].postln; }, '/wii/pitch');
OSCFunc({|msg|  ~amp1 = msg[1].postln; }, '/wii/accel');
OSCFunc({|msg|  ~attack = msg[1].postln; }, '/wii/roll');
OSCFunc({|msg|  ~release = msg[1].postln; }, '/wii/yaw');
OSCFunc({|msg|  ~sustain = msg[1].postln; }, '/wii/accel');
)