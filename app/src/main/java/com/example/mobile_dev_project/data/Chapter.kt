package com.example.mobile_dev_project.data

data class Chapter(
    val title: String,
    val content: String
)

// Flowers for Algernon (Progress Reports 1–7)
val mockChapters = listOf(
    Chapter(
        "progris riport 1",
        """
martch 5 1965

Dr. Strauss says I should write down what I think and remember. He says it’s like keeping a diary. I hope they use me for the experiment. Miss Kinnian says maybe they can make me smart. I want to be smart. My name is Charlie Gordon. I am 37 years old and 2 weeks ago was my brithday. I have nuthing more to rite now so I will close for today
""".trimIndent()
    ),
    Chapter(
        "progris riport 2",
        """
martch 6

I had a test today. I think I faled it. and I think that maybe now they wont use me. What happind is a nice young man was in the room and he had some white cards with ink spillled all over them. ...
""".trimIndent()
    ),
    Chapter(
        "progris riport 3",
        """
martch 7

Dr Strauss and Dr Nemur say it dont matter about the inkblots. I told them I dint spill the ink on the cards and I coudnt see anything in the ink. ...
""".trimIndent()
    ),
    Chapter(
        "progris riport 4",
        """
Mar 8

Their going to use me! 1m so exited I can hardly write. Dr Nemur and Dr
Strauss had a argament about it first. ...
""".trimIndent()
    ),
    Chapter(
        "progris riport 5",
        """
Mar 10

1m skared. Lots of people who work here and the nurses and the people who
gave me the tests came to bring me candy and wish me luck. ...
""".trimIndent()
    ),
    Chapter(
        "progress report 6",
        """
Mar 15

The operashun dint hurt. He did it while I was sleeping. They took off the
bandijis from my eyes and my head today so I can make a PROGRESS REPORT. ...
""".trimIndent()
    ),
    Chapter(
        "progress report 7",
        """
mar 19

Nothing is happining. I had lots of tests and different kinds of races with
Algernon. I hate that mouse. ...
""".trimIndent()
    )
)
