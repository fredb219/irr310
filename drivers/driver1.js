importPackage(Packages.com.irr310.server.js.objects); // assuming the Java class my.game.Player exists

var player = new Player("Jake");
player.gender = "female"; // this is a Java method!
player.setGender("male"); // this the same Java method.
player.age = 18; // this is a Java field
player.age += 3;

var player = new Player("Jane");
player.gender = "male";
player.gender = "female";
player.age = 19;
player.age += 2;

/*var count = Player.PLAYER_COUNT;
Player.PLAYER_COUNT += 2;*/
