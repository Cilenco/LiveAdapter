package com.cilenco.liveadapter.sample.models

import com.amulyakhare.textdrawable.util.ColorGenerator
import com.amulyakhare.textdrawable.TextDrawable



data class Book(val title: String, val author: String, val icon: TextDrawable) {

    companion object {
        var generator = ColorGenerator.MATERIAL

        var builder: TextDrawable.IBuilder = TextDrawable.builder()
            .beginConfig()
            .endConfig()
            .round()

        val SAMPLE_DATA = listOf(
            Book("The Hunger Games", "Suzanne Collins", builder.build("S", generator.randomColor)),
            Book("The World Unseen", "Shamim Sarif", builder.build("S", generator.randomColor)),
            Book("Mary Poppins", "P. L. Travers", builder.build("P", generator.randomColor)),
            Book("Harry Potter and the Goblet of Fire", "Joanne K. Rowling", builder.build("J", generator.randomColor)),
            Book("Lady Midnight", "Cassandra Clare", builder.build("C", generator.randomColor)),
            Book("The Hound of the Baskervilles", "Arthur Conan Doyle", builder.build("A", generator.randomColor)),
            Book("The Perks of Being a Wallflower", "Stephen Chbosky", builder.build("S", generator.randomColor)),
            Book("Pride and Prejudice", "Jane Austen", builder.build("J", generator.randomColor)),
            Book("Milk and Honey", "Rupi Kaur", builder.build("R", generator.randomColor)),
            Book("If I Stay", "Gayle Forman", builder.build("G", generator.randomColor)),
            Book("Paper Towns", "John Green", builder.build("J", generator.randomColor)),
            Book("A Game of Thrones", "George R. R. Martin", builder.build("G", generator.randomColor)),
            Book("Red Queen", "Victoria Aveyard", builder.build("V", generator.randomColor)),
            Book("The Picture of Dorian Gray", "Oscar Wilde", builder.build("O", generator.randomColor)),
            Book("A Court of Thorns and Roses", "Sarah J. Maas", builder.build("S", generator.randomColor)),
            Book("Love Letters to the Dead", "Ava Dellaira", builder.build("A", generator.randomColor)),
            Book("The Best of Roald Dahl", "Roald Dahl", builder.build("R", generator.randomColor)),
            Book("The Marble Collector", "Cecelia Ahern", builder.build("C", generator.randomColor)),
            Book("Edge of Eternity", "Ken Follett", builder.build("K", generator.randomColor)),
            Book("About a Boy", "Nick Hornby", builder.build("N", generator.randomColor))
        )
    }

}