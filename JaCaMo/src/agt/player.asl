+!start
    :   true
    <-  .print("Starting player and foes");
        .send("foe1", tell, port(9081));
        .send("foe2", tell, port(9082));
        .send("foe3", tell, port(9083));
        see(Walls);
        .print(Walls);
        !get_beliefs_from_list(Walls);
        +life(20);
        //loot(Result);
        //move_player("left");
        +player(listening).


// The agent listens the user and adds a belief on the instruction given by the user
+player(listening)
    <-  .print("I am listening");
        listen(Intent, ObjName, Direction, Time);
        +instruction(Intent, ObjName, Direction, Time);
        -player(listening);
        +player(waiting).

+player(waiting)
    : life(N)& N>0
    <-  -player(waiting);
        +player(listening).

+player(waiting)
    : true
    <-  .print("I am dead.");
        die.

// This goal is triggered when an instruction with intent "move_player" is added to the beliefs
+instruction(move_player, ObjName, Direction, Time)
    : not wall(Direction)
    <-  .print("Intent move_player");
        move_player(Direction);
        -instruction(move_player, ObjName, Direction, Time);
        .abolish(wall(_));
        .abolish(monster(_));
        .abolish(object(_));
        .wait(4000);
        see(Walls);
        !foeTurn;
        !get_beliefs_from_list(Walls).

// This goal is triggered when an instruction with intent "move_camera" is added to the beliefs
+instruction(move_camera, ObjName, Direction, Time)
    <- .print("Intent move_camera");
        move_camera(Direction);
        -instruction(move_camera, ObjName, Direction, Time).

// This goal is triggered when an instruction with intent "take_object" is added to the beliefs
+instruction(take_object, ObjName, Direction, Time)
    : object(ObjName)
    <- .print("Intent take_object");
        take_object(ObjName);
        -instruction(take_object, ObjName, Direction, Time);
        !foeTurn;
        +inventory(ObjName).

// This goal is triggered when an instruction with intent "open_chest" is added to the beliefs
+instruction(open_chest, ObjName, Direction, Time)
    : chest(Nb) & inventory(key(Nb))
    <- .print("Intent open_chest");
        open_chest;
        !foeTurn;
        -instruction(open_chest, ObjName, Direction, Time).

// This goal is triggered when an instruction with intent "hit" is added to the beliefs
+instruction(hit, ObjName, Direction, Time)
    : monster(here)
    <- .print("Intent hit");
        hit;
        !foeTurn;
        -instruction(hit, ObjName, Direction, Time).

// This goal is triggered when an instruction with intent "avoid" is added to the beliefs
+instruction(avoid, ObjName, Direction, Time)
    <- .print("Intent avoid");
        avoid;
        !foeTurn;
        -instruction(avoid, ObjName, Direction, Time).

// This goal is triggered when an instruction with intent "loot" is added to the beliefs
+instruction(loot, ObjName, Direction, Time)
    : dead("here")
    <- .print("Intent loot");
        loot(Result);
        -instruction(loot, ObjName, Direction, Time);
        !foeTurn;
        +inventory(Result).

// This goal is triggered when an instruction with intent "rest" is added to the beliefs
+instruction(rest, ObjName, Direction, Time)
    : life(N)
    <- .print("Intent rest");
        rest(Time, N, Result);
        -instruction(rest, ObjName, Direction, Time);
        -life(N);
        +life(Result);
        +elapsedTime(Time).

+elapsedTime(a_little)
    <-  -elapsedTime(a_little);
        !foeTurn.
+elapsedTime(more)
    <-  -elapsedTime(more);
        !foeTurn;
        .wait(4000);
        !foeTurn.
+elapsedTime(until_full)
    <-  -elapsedTime(until_full);
        !foeTurn;
        .wait(4000);
        !foeTurn;
        .wait(4000);
        !foeTurn.

// Take damage into account
+damage(N)
    : life(NN)
    <-  .print("Ouch !");
        -life(NN);
        -damage(N);
        NNN = NN - N;
        +life(NNN).

// After a time taking action, we send enemies a message to play (looking doesn't take time)
+!foeTurn
    <-  .print("Sending foe to play");
        .send("foe1", achieve, play);
        .send("foe2", achieve, play);
        .send("foe3", achieve, play).


// To not pass through walls and detect objects (for now, there are no object but you know, it's just a problem of time)
+!get_beliefs_from_list([]).
+!get_beliefs_from_list({}).
+!get_beliefs_from_list([Belief|BeliefTail])
    <-  +Belief;
        !get_beliefs_from_list(BeliefTail).


+instruction(Intent, ObjName, Direction, Time)
    <-  .print("I can't do that !");
        -instruction(Intent, ObjName, Direction, Time).


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }