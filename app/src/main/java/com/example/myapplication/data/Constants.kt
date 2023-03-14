package com.example.myapplication.data

import com.example.myapplication.R
import com.example.myapplication.model.Comics

object Constants {


    const val COMICS_ID = "comics_id"
    const val NIGHT_MODE = "night_mode"
    const val SHARED_PREFS = "shared_prefs"


    fun getComics(): ArrayList<Comics> {
        var comicsList = ArrayList<Comics>()

        val com1 = Comics(
            0,
            "Book",
            "It ends with us",
            R.drawable.cover_flowers,
            R.drawable.detail_background_it_ends_with_us,
            "It Ends With Us is a 2016 romance novel by Colleen Hoover. The novel tells the story of Lily Bloom and her doomed romance with Ryle Kincaid and traces her past history growing up in an abusive home, her fall into an abusive relationship, and her escape from that relationship."
        )
        comicsList.add(com1)

        val com2 = Comics(
            1,
            "Manga",
            "Climber",
            R.drawable.cover_climber,
            R.drawable.detail_background_climber,
            "В старшей школе Северной Ёкосуки, новый, необычный ученик – Мори Бунтаро. В первый же день он отдает предпочтение крыше здания, чем ее кабинетам, где со всех сторон сыплются расспросы. Чтобы отвязаться от надоедливых одноклассников, лихо спрыгивающих с карниза вниз по веревке, Мори совершает безумный поступок: без страховки забирается на крышу по трубе, да еще в таком месте, где это достаточно проблематично даже для тренированного человека! Одноклассник Миямото и Ониси-сенсей в ужасе и восторге, ведь они профессиональные скалолазы, а тут появляется такой способный ученик! Но Мори не разделяет их восторга и не принимает помощи. Нелюдимый и гордый, он всегда всего добивался в одиночку, и трудности скалолазания его не пугают. Он собирается покорять вершину за вершиной, с каждым разом поднимаясь все выше."
        )
        comicsList.add(com2)


        val com3 = Comics(
            2,
            "Manga",
            "Vagabond",
            R.drawable.cover_vagabonnd,
            R.drawable.detail_background_vagabond,
            "Growing up in 17th century Sengoku era Japan, Shinmen Takezou is shunned by the local villagers as a devil child due to his wild and violent nature. Running away from home with a fellow boy at age 17, Takezou joins the Toyotomi army to fight the Tokugawa clan at the battle of Sekigahara. However, the Tokugawa win a crushing victory, leading to nearly three hundred years of Shogunate rule. Takezou and his friend manage to survive the battle, and afterwards swear to do great things with their lives. But after their paths seperate, Takezou becomes a wanted criminal, and must change his name and his nature in order to escape an ignoble death. Based on the book \"Musashi\" by Eiji Yoshikawa, Vagabond is a fictional retelling of the life of Miyamoto Mushashi, often referred to as the \"Sword Saint\" - perhaps the most famous and successful of Japan's sword fighters."
        )
        comicsList.add(com3)

        val com4 = Comics(
            3,
            "Manga",
            "MF Ghost",
            R.drawable.cover_mf_ghost,
            R.drawable.detail_background_mf_ghost,
            "Конец 2020-х. По всему миру автомобили имеют всё больше систем автономного вождения, " +
                    "и всё меньше из них оснащаются ДВС. Вопреки этому, под руководством загадочного Рё Такахаши в" +
                    " Японии проводится гоночный чемпионат на исключительно бензиновых машинах, носящий кодовое название MFG. " +
                    "Вместо гоночных трасс – закрытые участки бывших общественных дорог, пострадавших в результате активности Фудзи." +
                    " Вместо трибун – дроны, ведущие онлайн-трансляцию для людей из любой точки планеты." +
                    " Главным героем этих событий становится 19-летний Каната, наполовину англичанин и наполовину японец, чей учитель ставит " +
                    "парадоксальную задачу: принять участие в MFG на маломощной машине и бросить вызов таким инженерным шедеврам, как Ferrari," +
                    " Lamborghini или Porsche."
        )
        comicsList.add(com4)

        val com5 = Comics(
            4,
            "Manga",
            "Blue Lock",
            R.drawable.cover_blue_lock,
            R.drawable.detail_background_blue_lock,
            "По сюжету, после проигрыша японской сборной на ЧМ 2018 был разработан специальный проект «Блю Лок» для «создания» лучшего и эгоистичного нападающего. Чтобы выжить в Синей тюрьме, необходимо «пройтись по трупам остальных», ведь вылет означает конец всей футбольной жизни... " +
                    "До конца дойдёт лишь 1 из 300 участников! Это манга про эгоистичный футбол!" +
                    " Lamborghini или Porsche."
        )
        comicsList.add(com5)



        return comicsList
    }
}