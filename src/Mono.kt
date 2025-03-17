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

