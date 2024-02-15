// Actions available:
// - [ ] move
// - [ ] hit

// Realism actions (not implemented):
// - [ ] play cards
// - [ ] sleep

+!start
    :true
    <-  .print("Starting monster");
        +life(10).

+!play
    : port(Port)
    <-  look(Port,Objects);
        !get_beliefs_from_list(Objects);
        !playing.

+life(N)
    : port(Port) & N<=0
    <-  die(Port);
        .print("I'll be back.");
        killAgent(this).

+!playing
    : port(Port) & player(here)
    <-  hit(Port);
        .abolish(wall(_));
        .abolish(player(_)).

+!playing
    : port(Port) & player(Direction)
    <-  move(Port, Direction);
        .abolish(wall(_));
        .abolish(player(_)).

+!playing
    : not wall(forward) & port(Port)
    <-  move(Port, forward);
        .abolish(wall(_));
        .abolish(player(_)).

+!playing
    : not wall(left) & port(Port)
    <-  move(Port, left);
        .abolish(wall(_));
        .abolish(playing);
        .abolish(player(_)).

+!playing
    : not wall(right) & port(Port)
    <-  move(Port, right);
        .abolish(wall(_));
        .abolish(player(_)).

+!playing
    : not wall(backward) & port(Port)
    <-  move(Port, backward);
        .abolish(wall(_));
        .abolish(player(_)).

+!playing
    : true
    <-  .print("I messed up...");
        .abolish(wall(_));
        .abolish(player(_)).


+damage(N)
    :life(NN)
    <-  -life(NN);
        -damage(N);
        NNN = NN - N;
        +life(NNN).

+!get_beliefs_from_list([]).
+!get_beliefs_from_list({}).
+!get_beliefs_from_list([Belief|BeliefTail])
    <-  +Belief;
        .print(Belief);
        !get_beliefs_from_list(BeliefTail).


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }