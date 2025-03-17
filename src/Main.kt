import kotlin.random.Random

fun main() {
    // Initialize the game board
    val board = listOf(
        Property("Start", 0, 0),
        Property("Purple", 100, 10),
        Property("Blue", 200, 20),
        Property("Green", 300, 30),
        Property("Yellow", 400, 40),
        Property("Orange", 500, 50),
        Property("Red", 600, 60),
        Property("Black", 700, 70)
    )

    // Initialize players with 8 initial tokens
    val tokens = mutableListOf("Car", "Hat", "Dog", "Boat", "Horse", "Thimble", "Shoe", "Iron")
    val players = mutableListOf<Player>()

    // Selection process for players to choose their tokens
    for (i in 1..8) { // Assuming 8 players for this example
        print("Enter the name for Player $i: ")
        val name = readLine()!!
        print("Choose a token for $name from $tokens: ")
        var token = readLine()!!
        while (token !in tokens) {
            println("Invalid token. Please choose from the list.")
            print("Choose a token for $name from $tokens: ")
            token = readLine()!!
        }
        players.add(Player(name, token))
        tokens.remove(token)
    }

    // Determine turn order based on dice rolls
    val turnOrder = players.map { player ->
        val roll = Random.nextInt(1, 7)
        println("${player.name} rolled a $roll")
        roll to player
    }.sortedByDescending { it.first }.map { it.second }

    println("Turn order:")
    turnOrder.forEach { println(it.name) }

    // Game loop
    while (true) {
        for (player in turnOrder) {
            val steps = Random.nextInt(1, 7) // Roll the dice
            val currentProperty = player.move(steps, board)
            displayBoard(turnOrder, board)

            if (currentProperty.owner == null) {
                player.buyProperty(currentProperty)
            } else {
                println("${currentProperty.name} is already owned by ${currentProperty.owner!!.token} (${currentProperty.owner!!.name}), pay ${currentProperty.rent}")
                player.balance -= currentProperty.rent
                currentProperty.owner!!.balance += currentProperty.rent
            }

            // Check for bankruptcy
            if (player.balance <= 0) {
                println("${player.token} (${player.name}) is bankrupt!")
                break
            }
        }
        break
    }
}