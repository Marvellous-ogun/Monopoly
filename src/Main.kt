import kotlin.random.Random

// Define the board
data class Property(
    val name: String,
    val cost: Int,
    val rent: Int,
    var owner: Player? = null
) {
    override fun toString(): String {
        return "$name ($cost$, Rent: $rent$)"
    }
}

class Player(
    val name: String,
    val token: String,
    var balance: Int = 1500
)
{
    var position: Int = 0
    val properties: MutableList<Property> = mutableListOf()

    fun move(steps: Int, board: List<Property>): Property {
        position = (position + steps) % board.size
        val currentProperty = board[position]
        println("$token ($name) landed on ${currentProperty.name}")
        return currentProperty
    }

    fun buyProperty(property: Property) {
        if (balance >= property.cost) {
            balance -= property.cost
            properties.add(property)
            property.owner = this
            println("$token ($name) bought ${property.name}")
        } else {
            println("$token ($name) cannot afford ${property.name}")
        }
    }
}

fun displayBoard(players: List<Player>, board: List<Property>) {
    val boardDisplay = board.mapIndexed { index, prop ->
        val tokens = players.filter { it.position == index }.joinToString(" ") { it.token }
        if (tokens.isNotEmpty()) "${prop.name} [$tokens]" else prop.name
    }
    println("Board: ${boardDisplay.joinToString(" -> ")}")
}

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