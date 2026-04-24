package com.example.bookhaven.preset;

import com.example.bookhaven.model.Book;
import com.example.bookhaven.model.Category;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.BookRepository;
import com.example.bookhaven.repository.CategoryRepository;
import com.example.bookhaven.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("devs")
public class BookHavenPreset {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public BookHavenPreset(CategoryRepository categoryRepository,
                           BookRepository bookRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JdbcTemplate jdbcTemplate) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void reseedDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbcTemplate.execute("TRUNCATE TABLE order_items");
        jdbcTemplate.execute("TRUNCATE TABLE orders");
        jdbcTemplate.execute("TRUNCATE TABLE cart_items");
        jdbcTemplate.execute("TRUNCATE TABLE cart");
        jdbcTemplate.execute("TRUNCATE TABLE book_categories");
        jdbcTemplate.execute("TRUNCATE TABLE books");
        jdbcTemplate.execute("TRUNCATE TABLE categories");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");

        seedDatabase();
    }

//    @PostConstruct
    public void init() {
        reseedDatabase();
    }

    private void seedDatabase() {

        // ── Admin User ─────────────────────────────────────
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("admin");
        admin.setActive(true);
        userRepository.save(admin);

        // ── User ─────────────────────────────────────
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        user.setUsername("User");
        user.setPassword(passwordEncoder.encode("user1234"));
        user.setRole("user");
        user.setActive(true);
        userRepository.save(user);

        // ── Categories ─────────────────────────────────────
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(null, "Action", null));
        categories.add(new Category(null, "Adventure", null));
        categories.add(new Category(null, "Comedy", null));
        categories.add(new Category(null, "Drama", null));
        categories.add(new Category(null, "Fantasy", null));
        categories.add(new Category(null, "Historical", null));
        categories.add(new Category(null, "Horror", null));
        categories.add(new Category(null, "Isekai", null));
        categories.add(new Category(null, "Josei", null));
        categories.add(new Category(null, "Mecha", null));
        categories.add(new Category(null, "Mystery", null));
        categories.add(new Category(null, "Music", null));
        categories.add(new Category(null, "Psychological", null));
        categories.add(new Category(null, "Romance", null));
        categories.add(new Category(null, "School", null));
        categories.add(new Category(null, "Sci-Fi", null));
        categories.add(new Category(null, "Seinen", null));
        categories.add(new Category(null, "Shojo", null));
        categories.add(new Category(null, "Shonen", null));
        categories.add(new Category(null, "Slice of Life", null));
        categories.add(new Category(null, "Sports", null));
        categories.add(new Category(null, "Supernatural", null));
        categories.add(new Category(null, "Thriller", null));

        categoryRepository.saveAll(categories);

        Category action        = categories.get(0);
        Category adventure     = categories.get(1);
        Category comedy        = categories.get(2);
        Category drama         = categories.get(3);
        Category fantasy       = categories.get(4);
        Category historical    = categories.get(5);
        Category horror        = categories.get(6);
        Category isekai        = categories.get(7);
        Category josei         = categories.get(8);
        Category mecha         = categories.get(9);
        Category mystery       = categories.get(10);
        Category music         = categories.get(11);
        Category psychological = categories.get(12);
        Category romance       = categories.get(13);
        Category school        = categories.get(14);
        Category sciFi         = categories.get(15);
        Category seinen        = categories.get(16);
        Category shojo         = categories.get(17);
        Category shonen        = categories.get(18);
        Category sliceOfLife   = categories.get(19);
        Category sports        = categories.get(20);
        Category supernatural  = categories.get(21);
        Category thriller      = categories.get(22);

        // ── Books ──────────────────────────────────────────
        List<Book> books = new ArrayList<>();

        // Multi genre books 1
        books.add(new Book(null,"Jujutsu Kaisen","Gege Akutami",700.00,"Jujutsu Kaisen follows Yuji Itadori, a high school student who swallows a cursed object and becomes the host of the powerful curse Ryomen Sukuna. He joins a secret school of jujutsu sorcerers to hunt dangerous curses and collect Sukuna’s scattered fingers, knowing his mission will eventually cost him his life.","https://comicvine.gamespot.com/a/uploads/scale_large/11133/111333163/8846037-jujutsu-kaisen-22-jp.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Vinland Saga","Makoto Yukimura",750.00,"Vinland Saga follows Thorfinn, a young Viking warrior who joins a band of mercenaries led by Askeladd to avenge his father’s death. As he grows up in a brutal world of war and conquest, Thorfinn struggles to find purpose beyond revenge and seeks a peaceful land called Vinland.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9623711-w.jpg",List.of(action, historical, seinen),null,null));
        books.add(new Book(null,"Kaguya Sama Love is War","Aka Akasaka",800.00,"Kaguya-sama: Love Is War follows Kaguya Shinomiya and Miyuki Shirogane, two elite student council leaders who secretly love each other but refuse to confess. They engage in over-the-top psychological battles to force the other to admit their feelings first, turning romance into a hilarious war of pride and strategy.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/7459618-15.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Blue Lock","Muneyuki Kaneshiro",750.00,"Blue Lock follows Yoichi Isagi, a high school striker invited to join Blue Lock, a ruthless training program designed to create Japan’s ultimate egoist forward. Competing against other talented strikers, Isagi must evolve his skills and crush his rivals to survive the intense elimination system and become the world’s best striker.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111450787/9364117-9763695451-710Pn.jpg",List.of(sports, psychological, shonen),null,null));
        books.add(new Book(null,"Made in Abyss","Akihito Tsukushi",1500.00,"Made in Abyss follows Riko, a young orphan who dreams of becoming a Cave Raider like her mother, and Reg, a mysterious robot boy she discovers in the Abyss. Together, they descend into the massive, dangerous pit to search for Riko’s mother, facing deadly creatures, harsh environments, and the terrifying curse of the Abyss.","https://comicvine.gamespot.com/a/uploads/scale_large/11135/111359134/8698568-madeinabyss%2311-page1.jpg",List.of(adventure, fantasy, mystery),null,null));

        books.add(new Book(null,"Fruits Basket","Natsuki Takaya",1300.00,"Fruits Basket follows Tohru Honda, a kind-hearted girl who ends up living with members of the Sohma family, who are cursed to transform into animals of the Chinese zodiac when hugged by the opposite sex. As Tohru grows closer to Yuki Sohma and Kyo Sohma, she helps them confront their emotional wounds and break free from the family’s dark curse.","https://comicvine.gamespot.com/a/uploads/scale_large/2/21294/568223-fruits_basket_manga.jpg",List.of(romance, supernatural, shojo),null,null));
        books.add(new Book(null,"Akira","Katsuhiro Otomo",1500.00,"Akira follows Kaneda, a biker gang leader in a dystopian Neo-Tokyo, whose friend Tetsuo Shima gains dangerous psychic powers after a mysterious accident. As Tetsuo’s abilities spiral out of control, Kaneda becomes caught in a violent conflict involving the military, secret experiments, and the catastrophic force known as Akira.","https://comicvine.gamespot.com/a/uploads/scale_large/13/136525/5992703-4.jpg",List.of(sciFi, action, seinen),null,null));
        books.add(new Book(null,"Barakamon","Satsuki Yoshino",830.00,"Barakamon follows Seishuu Handa, a talented but hot-headed calligrapher who is sent to a remote island after punching a critic. Surrounded by quirky villagers, including the energetic child Naru Kotoishi, Handa gradually finds inspiration, personal growth, and a new perspective on life and art.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111450787/9021886-9607533781-91Jqg.jpg",List.of(sliceOfLife, comedy, seinen),null,null));
        books.add(new Book(null,"Parasyte","Hitoshi Iwaaki",1100.00,"Parasyte follows Shinichi Izumi, a high school student whose right hand is taken over by an alien parasite named Migi. Forced to coexist, the two form an uneasy alliance and battle other parasites that prey on humans, while Shinichi struggles to hold on to his humanity as the threat grows.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5007449-08.jpg",List.of(horror, psychological, sciFi),null,null));
        books.add(new Book(null,"Haikyuu","Haruichi Furudate",700.00,"Haikyu!! follows Shoyo Hinata, a determined but short volleyball player who joins Karasuno High to chase his dream of becoming the best. Teaming up with his former rival Tobio Kageyama, Hinata works with his teammates to rebuild the team and compete against powerful schools on their journey to the national tournament.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/4134057-12.jpg",List.of(sports, school, shonen),null,null));

        books.add(new Book(null,"Noragami","Adachitoka",800.00,"Noragami follows Yato, a minor god who dreams of gaining worshippers and building his own shrine. After crossing paths with Hiyori Iki and partnering with his weapon spirit Yukine, Yato takes odd jobs while battling dangerous phantoms and confronting his dark past.","https://comicvine.gamespot.com/a/uploads/scale_large/11/113021/3715063-56588%20v01_front.jpg",List.of(supernatural, action, shonen),null,null));
        books.add(new Book(null,"Steins;Gate","5pb",1800.00,"Steins;Gate follows Rintaro Okabe, a self-proclaimed mad scientist who accidentally discovers a way to send messages to the past using a modified microwave. As he and his friends experiment with time travel, Okabe becomes entangled in dangerous conspiracies and must race against time to prevent tragic consequences.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9866919-wwww.jpg",List.of(sciFi, thriller, psychological),null,null));
        books.add(new Book(null,"Orange","Ichigo Takano",850.00,"Orange follows Naho Takamiya, a high school girl who receives letters from her future self warning her about regrets she must avoid. Determined to change fate, she and her friends try to support transfer student Kakeru Naruse and prevent the tragic future described in the letters.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5885500-05.jpg",List.of(drama, romance, school),null,null));
        books.add(new Book(null,"Gantz","Hiroya Oku",1900.00,"Gantz follows Kei Kurono, a teenager who dies in an accident and is revived in a mysterious room with a black sphere called Gantz. Forced into deadly missions alongside other resurrected people, Kurono must hunt dangerous aliens to survive while trying to understand the brutal rules of the game.","https://comicvine.gamespot.com/a/uploads/scale_large/13/136525/6218901-33.jpg",List.of(action, sciFi, seinen),null,null));
        books.add(new Book(null,"Yotsuba&!","Kiyohiko Azuma",900.00,"Yotsuba&! follows Yotsuba Koiwai, an energetic and curious young girl who explores everyday life with endless enthusiasm. With the help of her father and friendly neighbors, Yotsuba discovers new experiences, turning ordinary moments into fun and heartwarming adventures.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/3033707-04.jpg",List.of(sliceOfLife, comedy),null,null));

        books.add(new Book(null,"Mairimashita! Iruma-kun","Kei Sanbe",1500.00,"Erased follows Satoru Fujinuma, a struggling manga artist with the ability to travel back in time moments before disasters occur. When a tragedy sends him back to his childhood, he must uncover the truth behind a series of kidnappings and save his classmates to change the future.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6637745-07.jpg",List.of(mystery, thriller, supernatural),null,null));
        books.add(new Book(null,"Blue Period","Tsubasa Yamaguchi",750.00,"Blue Period follows Yatora Yaguchi, a high school student who discovers a passion for painting after being inspired by art. Determined to enter a prestigious art university, he dives into the demanding world of art, pushing himself to grow creatively while confronting self-doubt and competition.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6601764-01.jpg",List.of(drama, school, seinen),null,null));
        books.add(new Book(null,"Is it Wrong to Try to Pick Up Girls in a Dungeon?","Tappei Nagatsuki",1000.00,"Re:Zero − Starting Life in Another World follows Subaru Natsuki, a shut-in who is suddenly transported to a fantasy world and discovers he can return to life after dying. As he forms bonds with Emilia and others, Subaru repeatedly relives painful timelines, using his ability to change fate and protect those he cares about.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9752995-w.jpg",List.of(isekai, fantasy, psychological),null,null));
        books.add(new Book(null,"Nana","Ai Yazawa",1300.00,"Nana follows Nana Osaki and Nana Komatsu, two women with the same name who meet by chance and become roommates in Tokyo. As they pursue their dreams in music and love, their lives intertwine through friendships, relationships, and the struggles of adulthood.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/4079726-13.jpg",List.of(josei, romance, drama),null,null));
        books.add(new Book(null,"Neon Genesis Evangelion","Yoshiyuki Sadamoto",1450.00,"Neon Genesis Evangelion follows Shinji Ikari, a reluctant teenager recruited by his father to pilot a giant bio-mechanical robot called an Evangelion. As he fights mysterious beings known as Angels, Shinji struggles with loneliness, identity, and the psychological weight of protecting humanity.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/3238178-03.jpg",List.of(mecha, psychological, sciFi),null,null));

        books.add(new Book(null,"Detective Conan","Gosho Aoyama",750.00,"Detective Conan follows Shinichi Kudo, a brilliant teenage detective who is transformed into a child after being poisoned by a mysterious organization. Under the alias Conan Edogawa, he solves cases while secretly searching for the people responsible and trying to regain his original body.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/8512998-detconan-v2-005_001.jpg",List.of(mystery, shonen),null,null));
        books.add(new Book(null,"Beck: Mongolian Chop Squad","Harold Sakuishi",700.00,"Beck: Mongolian Chop Squad follows Yukio Tanaka, a bored teenager whose life changes after meeting guitarist Ryusuke Minami. As Tanaka joins a rock band called BECK, he develops his musical talent while chasing dreams of success in the competitive music scene..","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2452044-01_cropped.jpg",List.of(music, sliceOfLife, seinen),null,null));
        books.add(new Book(null,"Kimi ni Todoke","Karuho Shiina",950.00,"Kimi ni Todoke: From Me to You follows Sawako Kuronuma, a shy girl misunderstood because of her eerie appearance. After befriending the popular and kind Shota Kazehaya, Sawako begins to open up, form friendships, and experience love for the first time.","https://comicvine.gamespot.com/a/uploads/scale_large/11120/111205788/4261089-21.jpg",List.of(romance, school, shojo),null,null));
        books.add(new Book(null,"Blame!","Tsutomu Nihei",850.00,"Blame! follows Killy, a quiet wanderer traveling through a vast, decaying megastructure in search of humans with the Net Terminal Gene. As he explores the endless city, Killy battles hostile cyborgs and dangerous entities while uncovering fragments of a lost civilization.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9865011-1974746305.jpg",List.of(sciFi, action, mystery),null,null));
        books.add(new Book(null,"Golden Kamuy","Satoru Noda",1100.00,"Golden Kamuy follows Saichi Sugimoto, a veteran of the Russo-Japanese War who searches for a hidden stash of Ainu gold to fulfill a promise. Teaming up with Ainu girl Asirpa, he embarks on a dangerous treasure hunt while pursued by soldiers, prisoners, and other rivals.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5287524-01.jpg",List.of(historical, adventure, seinen),null,null));

        // Multi genre books 2
        books.add(new Book(null,"Dorohedoro","Q Hayashida",1100.00,"Dorohedoro follows Caiman, a man with a reptile head and no memories who searches for the sorcerer responsible for his transformation. With the help of his friend Nikaido, he hunts magic users while navigating a violent world of dark humor, mystery, and chaos.","https://covers.openlibrary.org/b/id/11898356-L.jpg",List.of(action, fantasy, seinen),null,null));
        books.add(new Book(null,"Toradora","Yuyuko Takemiya",2000.00,"Toradora! follows Ryuuji Takasu, a gentle student with an intimidating face who forms an unlikely alliance with the short-tempered Taiga Aisaka. As they help each other pursue their respective crushes, their relationship gradually deepens, leading to unexpected feelings and personal growth.","https://covers.openlibrary.org/b/id/7827344-L.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Ajin","Gamon Sakurai",850.00,"Ajin: Demi-Human follows Kei Nagai, a high school student who discovers he is an immortal being known as an Ajin after surviving a fatal accident. Hunted by the government for experimentation, Kei is drawn into a violent conflict with other Ajin, including the dangerous Sato.","https://covers.openlibrary.org/b/id/10866386-L.jpg",List.of(action, horror, psychological),null,null));
        books.add(new Book(null,"No Longer Human","Osamu Dazai",689.00,"No Longer Human follows Oba Yozo, a troubled man who feels disconnected from society and hides his despair behind humor. As his life spirals through addiction, failed relationships, and isolation, Yozo struggles with his identity and the feeling that he no longer qualifies as human.","https://covers.openlibrary.org/b/id/15142271-L.jpg",List.of(psychological, drama),null,null));
        books.add(new Book(null,"Chihayafuru","Yuki Suetsugu",750.00,"Chihayafuru follows Chihaya Ayase, a high school girl who becomes passionate about competitive karuta after meeting a talented player. She forms a school club with her friends and aims to become the best in Japan, balancing friendships, rivalry, and her growing love for the game.","https://covers.openlibrary.org/b/id/13784023-L.jpg",List.of(josei, sports, drama),null,null));

        books.add(new Book(null,"Fire Force","Atsushi Ohkubo",850.00,"Fire Force follows Shinra Kusakabe, a young firefighter with the ability to ignite his feet who joins Special Fire Force Company 8. As he battles infernals and investigates mysterious spontaneous human combustion, Shinra seeks the truth behind his family’s death and the secrets of the Fire Force.","https://covers.openlibrary.org/b/id/14054802-L.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Soul Eater","Atsushi Ohkubo",850.00,"Soul Eater follows Maka Albarn, a meister who partners with her living weapon Soul Eater Evans at Death Weapon Meister Academy. Together with their friends, they hunt evil souls and witches to create a powerful Death Scythe while facing dangerous enemies and supernatural threats.","https://covers.openlibrary.org/b/id/13716281-L.jpg",List.of(shonen, action, supernatural),null,null));
        books.add(new Book(null,"Overlord","Kugane Maruyama",700.00,"Overlord follows Momonga, a gamer who becomes trapped in a virtual world after his favorite online game shuts down. Taking the name Ainz Ooal Gown, he commands powerful NPC servants and begins conquering the new world while searching for other players.","https://covers.openlibrary.org/b/id/11887007-L.jpg",List.of(isekai, fantasy, action),null,null));
        books.add(new Book(null,"Food Wars!: Shokugeki no Soma","Yuto Tsukuda",700.00,"Food Wars!: Shokugeki no Soma follows Soma Yukihira, a talented young chef who enrolls in an elite culinary academy where students compete in intense cooking battles. As Soma challenges skilled rivals, he pushes his creativity and skills to rise through the ranks and surpass his father.","https://covers.openlibrary.org/b/id/13947095-L.jpg",List.of(shonen, comedy, sliceOfLife),null,null));
        books.add(new Book(null,"Mob Psycho 100","ONE",700.00,"Mob Psycho 100 follows Shigeo Kageyama, a quiet middle schooler with overwhelming psychic powers who tries to live a normal life. Working under the self-proclaimed psychic Reigen Arataka, Mob faces spirits and other espers while learning to understand his emotions and grow as a person.","https://covers.openlibrary.org/b/id/14708774-L.jpg",List.of(supernatural, comedy, action),null,null));

        books.add(new Book(null,"Blue Exorcist","Kazue Kato",750.00,"Blue Exorcist follows Rin Okumura, a teenager who discovers he is the son of Satan after gaining demonic powers. Determined to fight against his fate, he enrolls in an exorcist academy to become an exorcist and battle demons while hiding his true identity.","https://covers.openlibrary.org/b/id/13748867-L.jpg",List.of(supernatural, action, shonen),null,null));
        books.add(new Book(null,"Witch Hat Atelier","Kamome Shirahama",850.00,"Witch Hat Atelier follows Coco, a young girl who dreams of becoming a witch despite being born without magic. After accidentally casting a forbidden spell, she becomes the apprentice of the witch Qifrey and enters a magical world filled with wonder, secrets, and danger.","https://covers.openlibrary.org/b/id/10156742-L.jpg",List.of(fantasy, adventure, seinen),null,null));
        books.add(new Book(null,"Oyasumi PunPun","Inio Asano",700.00,"Oyasumi Punpun follows Punpun Onodera, a young boy navigating childhood, adolescence, and adulthood while dealing with family issues and emotional struggles. As he grows older, Punpun’s relationships and dreams become increasingly complicated, leading him down a dark and introspective path.","https://covers.openlibrary.org/b/id/15164180-L.jpg",List.of(romance, comedy, shojo),null,null));
        books.add(new Book(null,"Pokemon Adventures","Hidenori Kusaka",800.00,"Pokémon Adventures follows Red, a skilled Pokémon trainer who sets out on a journey to become stronger and protect others. Alongside allies and rivals, he battles powerful opponents, confronts villainous teams, and uncovers larger threats across different regions.","https://covers.openlibrary.org/b/id/813666-L.jpg",List.of(action, fantasy, shonen),null,null));
        books.add(new Book(null,"Dr Stone","Riichiro Inagaki",750.00,"SDr. Stone follows Senku Ishigami, a brilliant student who awakens thousands of years after humanity is mysteriously turned to stone. Using the power of science, Senku works to rebuild civilization from scratch while facing challenges from nature and those who oppose his vision.","https://covers.openlibrary.org/b/id/13481987-L.jpg",List.of(adventure, sciFi, shonen),null,null));

        books.add(new Book(null,"Horimiya","HERO",629.00,"Hidden personalities.","https://covers.openlibrary.org/b/id/14868029-L.jpg",List.of(romance, sliceOfLife, school),null,null));
        books.add(new Book(null,"Hinamatsuri","Masao Ohtake",609.00,"Psychic girl comedy.","https://covers.openlibrary.org/b/id/14764524-L.jpg",List.of(comedy, supernatural, seinen),null,null));
        books.add(new Book(null,"Kingdom","Yasuhisa Hara",799.00,"Ancient China war.","https://covers.openlibrary.org/b/id/15169870-L.jpg",List.of(historical, action, seinen),null,null));
        books.add(new Book(null,"Yamada Kun and Seven Witches","Miki Yoshikawa",599.00,"Body swap school.","https://covers.openlibrary.org/b/id/13581723-L.jpg",List.of(school, supernatural, comedy),null,null));
        books.add(new Book(null,"Dragon Ball","Akira Toriyama",569.00,"Countryside school life.","https://comicvine.gamespot.com/a/uploads/scale_large/11136/111369808/6746167-viz%2042.jpg",List.of(sliceOfLife, comedy, school),null,null));

        books.add(new Book(null,"One Piece","Eiichiro Oda",649.00,"Wizard adventure.","https://comicvine.gamespot.com/a/uploads/scale_large/11136/111369808/6759923-volume%2059.jpg",List.of(adventure, fantasy, shonen),null,null)); // One Piece
        books.add(new Book(null,"Naruto","Masashi Kishimoto",679.00,"Fashion romance.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/3232128-47.jpg",List.of(josei, romance, drama),null,null)); // Naruto
        books.add(new Book(null,"Bleach","Tite Kubo",629.00,"Deadly diary game.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2568500-25.jpg",List.of(thriller, psychological, supernatural),null,null)); // Dragon Ball
        books.add(new Book(null,"Fairy Tail","Hiro Mashima",569.00,"Absurd school comedy.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6154483-63.jpg",List.of(comedy, school, sliceOfLife),null,null));
        books.add(new Book(null,"Grand Blue","Kenji Inoue",599.00,"College diving comedy.","https://covers.openlibrary.org/b/id/14277134-L.jpg",List.of(comedy, sliceOfLife, seinen),null,null));

        // Multi genre books 3
        books.add(new Book(null,"Nichijou","Keiichi Arawi",569.00,"Absurd school comedy.","https://covers.openlibrary.org/b/id/15091329-L.jpg",List.of(comedy, school, sliceOfLife),null,null));
        books.add(new Book(null,"Initial D","Yoshiki Nakamura",629.00,"Showbiz revenge story.","https://covers.openlibrary.org/b/id/2260869-L.jpg",List.of(shojo, drama, romance),null,null));
        books.add(new Book(null,"My Hero Academia","Junji Ito",699.00,"Town cursed by spirals.","https://covers.openlibrary.org/b/id/8612351-L.jpg",List.of(horror, mystery, supernatural),null,null));
        books.add(new Book(null,"Delicious in Dungeon","Ryoko Kui",659.00,"Dungeon cooking adventure.","https://covers.openlibrary.org/b/id/14619026-L.jpg",List.of(fantasy, adventure, comedy),null,null));
        books.add(new Book(null,"Baby Steps","Hikaru Katsuki",619.00,"Tennis improvement journey.","https://covers.openlibrary.org/b/id/10359215-L.jpg",List.of(sports, school, shonen),null,null));

        books.add(new Book(null,"Prince of Tennis","Naoki Urasawa",719.00,"Robot murder mystery.","https://covers.openlibrary.org/b/id/7769202-L.jpg",List.of(mystery, sciFi, seinen),null,null)); // Prince of Tennis
        books.add(new Book(null,"Kuragehime","Akiko Higashimura",599.00,"Otaku girls life.","https://covers.openlibrary.org/b/id/8432457-L.jpg",List.of(josei, comedy, sliceOfLife),null,null)); // Initial D
        books.add(new Book(null,"Inuyasha","Rumiko Takahashi",679.00,"Feudal demon battles.","https://covers.openlibrary.org/b/id/7779332-L.jpg",List.of(action, supernatural, adventure),null,null));
        books.add(new Book(null,"Uzumaki","Inio Asano",699.00,"Dark coming of age.","https://covers.openlibrary.org/b/id/863043-L.jpg",List.of(seinen, psychological, drama),null,null));
        books.add(new Book(null,"K-On","Kakifly",599.00,"School band life.","https://covers.openlibrary.org/b/id/8433756-L.jpg",List.of(music, comedy, sliceOfLife),null,null));

        books.add(new Book(null,"Toriko","Mitsutoshi Shimabukuro",589.00,"Gourmet hunter adventure.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2914909-07.jpg",List.of(adventure, action, shonen),null,null));
        books.add(new Book(null,"Snow White with the Red Hair","Sorata Akizuki",599.00,"Fantasy romance.","https://covers.openlibrary.org/b/id/8798610-L.jpg",List.of(shojo, fantasy, romance),null,null));
        books.add(new Book(null,"Gintama","Masamune Shirow",799.00,"Cyber police.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9582858-w.jpg",List.of(sciFi, action, seinen),null,null)); // MF Ghost
        books.add(new Book(null,"Nisekoi","Junji Ito",679.00,"Immortal terrifying girl.","https://covers.openlibrary.org/b/id/13007378-L.jpg",List.of(horror, supernatural, mystery),null,null));
        books.add(new Book(null,"Flying Witch","Chihiro Ishizuka",579.00,"Relaxing witch life.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/7599049-09.jpg",List.of(sliceOfLife, supernatural, comedy),null,null)); // Bleach

        books.add(new Book(null,"Slam Dunk","Takehiko Inoue",679.00,"Basketball classic.","https://comicvine.gamespot.com/a/uploads/scale_large/11190/111908610/9618973-00106580317409____1__1200x1200.jpg",List.of(sports, school, shonen),null,null));
        books.add(new Book(null,"Major","Kaoru Mori",719.00,"Central Asia brides.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5625123-78.jpg",List.of(historical, romance, seinen),null,null)); // Pokemon Adventures
        books.add(new Book(null,"No Game No Life","Yuu Kamiya",619.00,"Gamers rule world.","https://comicvine.gamespot.com/a/uploads/scale_large/11/113021/3830922-90588%20v01_front.jpg",List.of(isekai, fantasy, comedy),null,null));
        books.add(new Book(null,"Homunculus","Hideo Yamamoto",699.00,"Trepanation mind horror.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9412315-001.jpg",List.of(psychological, seinen, thriller),null,null));
        books.add(new Book(null,"Given","Natsuki Kizu",629.00,"Band romance drama.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6926960-02.jpg",List.of(music, romance, drama),null,null));

        books.add(new Book(null,"Bocchi the Rock!","Sho Aimoto",609.00,"Supernatural detective kids.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111450787/8734629-9180742667-81CT9.jpg",List.of(supernatural, mystery, shonen),null,null));
        books.add(new Book(null,"MF Ghost","Inio Asano",649.00,"Young adult life.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111450787/8831354-2413302238-97840.jpg",List.of(drama, sliceOfLife, seinen),null,null));
        books.add(new Book(null,"So I'm a Spider So What","Okina Baba",639.00,"Reborn as spider.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/7528820-08.jpg",List.of(isekai, fantasy, adventure),null,null));
        books.add(new Book(null,"Wangan Midnight","Naoki Urasawa",759.00,"Cult conspiracy thriller.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5555405-12.jpg",List.of(thriller, mystery, seinen),null,null));
        books.add(new Book(null,"Yowamushi Pedal","Wataru Watanabe",599.00,"Cycling competition.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/8054563-68.5.jpg",List.of(sports, school, shonen),null,null));

        // Multi genre books 4
        books.add(new Book(null,"Daiya no A: Act II","Yuji Kaku",649.00,"Ninja survival island.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111450787/8301693-0180840827-97840.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Black Clover","Chica Umino",619.00,"College romance.","https://comicvine.gamespot.com/a/uploads/scale_large/11161/111610434/8698775-1142804605-97840.jpg",List.of(josei, romance, sliceOfLife),null,null));
        books.add(new Book(null,"Solo Leveling","Gamon Sakurai",689.00,"Immortal hunted humans.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9891351-wwww.jpg",List.of(action, horror, seinen),null,null));
        books.add(new Book(null,"Sket Dance","Kenta Shinohara",579.00,"School helpers club.","https://comicvine.gamespot.com/a/uploads/scale_large/11/113021/2929121-10426_v22_front.jpg",List.of(comedy, school, shonen),null,null));
        books.add(new Book(null,"Ranma 1/2","Kanata Yanagino",629.00,"Raised by undead.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2038875-14.jpg",List.of(fantasy, adventure, isekai),null,null));

        books.add(new Book(null,"Sun Ken Rock","Boichi",689.00,"Gang leader rise.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5413845-01.jpg",List.of(seinen, action, drama),null,null));
        books.add(new Book(null,"Tsurezure Children","Toshiya Wakabayashi",569.00,"Multiple couples.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6544517-12.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Classroom of the Elites","Looseboy",599.00,"Hidden killer school.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111458479/9329819-6093208819-81C6y.jpg",List.of(mystery, thriller, school),null,null));
        books.add(new Book(null,"Yu-Gi-Oh","Shinichi Ishizuka",689.00,"Jazz saxophonist dream.","https://comicvine.gamespot.com/a/uploads/scale_large/13/136525/5183590-30.jpg",List.of(music, drama, seinen),null,null));
        books.add(new Book(null,"Shangri-La Frontier","Shinichi Sakamoto",699.00,"French revolution drama.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/7705419-01.jpg",List.of(historical, drama, seinen),null,null));

        books.add(new Book(null,"Alice in Borderland","Haro Aso",649.00,"Deadly game world.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5577050-01.jpg",List.of(thriller, psychological, mystery),null,null));
        books.add(new Book(null,"Natsume Book of Friends","Yuki Midorikawa",599.00,"Boy sees spirits.","https://comicvine.gamespot.com/a/uploads/scale_large/13/136525/6157356-22.jpg",List.of(supernatural, sliceOfLife, shojo),null,null));
        books.add(new Book(null,"D-Frag!","Tomoya Haruno",559.00,"School game club.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/4009450-01.jpg",List.of(comedy, school, sliceOfLife),null,null));
        books.add(new Book(null,"The Fragrant Flower Blooms With Dignity","Tony Valente",639.00,"Magic world adventure.","https://comicvine.gamespot.com/a/uploads/scale_large/11145/111450787/8603347-0891682429-97840.jpg",List.of(fantasy, adventure, shonen),null,null));
        books.add(new Book(null,"Ichi the Witch","Junji Ito",689.00,"Short horror stories.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/10000448-wwww.jpg",List.of(horror, mystery, thriller),null,null));

        books.add(new Book(null,"Ping Pong","Taiyou Matsumoto",569.00,"Competitive table tennis.","https://comicvine.gamespot.com/a/uploads/scale_large/7/71975/4320084-1.jpg",List.of(sports, drama, seinen),null,null));
        books.add(new Book(null,"Say I Love You","Kanae Hazuki",579.00,"School romance drama.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/4764775-09.jpg",List.of(romance, school, shojo),null,null));
        books.add(new Book(null,"Kaichō wa Maid-sama!","Hiro Fujiwara",699.00,"Rebellion mechs.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/3053045-12.jpg",List.of(mecha, action, thriller),null,null));
        books.add(new Book(null,"Usagi Drop","Yumi Unita",629.00,"Single father story.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5350422-01.jpg",List.of(sliceOfLife, drama, josei),null,null));
        books.add(new Book(null,"Kono Oto Tomare!","Junya Inoue",659.00,"Island survival bombs.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5252182-01.jpg",List.of(thriller, action, seinen),null,null)); //

        books.add(new Book(null,"Eyeshield 21","Riichiro Inagaki",589.00,"American football.","https://comicvine.gamespot.com/a/uploads/scale_large/13/136525/5409691-36.jpg",List.of(sports, comedy, shonen),null,null));
        books.add(new Book(null,"Magi","Shinobu Ohtaka",629.00,"Dungeon adventures.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/5783971-22.jpg",List.of(adventure, fantasy, shonen),null,null));
        books.add(new Book(null,"PTSD Radio","Masaaki Nakayama",629.00,"Urban horror anthology.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6456780-01.jpg",List.of(horror, psychological, mystery),null,null));
        books.add(new Book(null,"Nodame Cantabile","Tomoko Ninomiya",619.00,"Classical music romance.","https://comicvine.gamespot.com/a/uploads/scale_large/11130/111303210/7255717-3.jpg",List.of(music, romance, josei),null,null));
        books.add(new Book(null,"Gakuen Babysitters","Yoshiki Tonogai",589.00,"Masked death game.","https://comicvine.gamespot.com/a/uploads/scale_large/11159/111592787/9922507-cover.jpg",List.of(thriller, horror, mystery),null,null));

        // Multi genre books 5
        books.add(new Book(null,"Chainsaw Man","Tatsuki Fujimoto",599.00,"Devil hunter chaos.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/7265340-06.jpg",List.of(action, horror, shonen),null,null));
        books.add(new Book(null,"We Never Learn","Yuyuko Takemiya",579.00,"Romantic school chaos.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/7597425-19.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Vagabond","Takehiko Inoue",799.00,"Samurai philosophical journey.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2938580-23.jpg",List.of(seinen, historical, action),null,null));
        books.add(new Book(null,"Kuroko Basketball","Tadatoshi Fujimaki",629.00,"Generation of miracles.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2914835-11.jpg",List.of(sports, school, shonen),null,null));
        books.add(new Book(null,"Attack on Titan","Kaiu Shirai",649.00,"Escape orphanage.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/8205713-34.jpg",List.of(mystery, thriller, shonen),null,null));

        books.add(new Book(null,"A Silent Voice","Yoshitoki Oima",599.00,"Bullying redemption.","https://comicvine.gamespot.com/a/uploads/scale_large/11139/111391600/8998180-7876198521-71iep.jpg",List.of(drama, romance, school),null,null));
        books.add(new Book(null,"Super Psychic Policeman Chojo","Spike Chunsoft",599.00,"Killing school mystery.","https://comicvine.gamespot.com/a/uploads/scale_large/11154/111547112/10009432-7732256061-81eK2.jpg",List.of(thriller, mystery, school),null,null));
        books.add(new Book(null,"Kimetsu no Yaiba","Yoshikazu Yasuhiko",799.00,"Robot war.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6448938-11.jpg",List.of(mecha, sciFi, action),null,null));
        books.add(new Book(null,"Bakuman","Tsugumi Ohba",589.00,"Supernatural slice life.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/2805948-08.jpg",List.of(supernatural, sliceOfLife, comedy),null,null));
        books.add(new Book(null,"3D Kanojo, Real Girl: Shinsōban","Ichigo Takano",619.00,"Future romance drama.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6927067-13.jpg",List.of(romance, drama, supernatural),null,null)); // Attack on Titan

        books.add(new Book(null,"Azumanga Daioh","Kazutaka Kodaka",689.00,"Cyberpunk criminals.","https://comicvine.gamespot.com/a/uploads/scale_large/0/3125/3596108-azu1.jpg",List.of(sciFi, action, thriller),null,null));
        books.add(new Book(null,"Katekyo Hitman Reborn!","HERO",609.00,"School romance life.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/3121020-41.jpg",List.of(romance, sliceOfLife, school),null,null)); // Lovely Complex
        books.add(new Book(null,"Beastars","Paru Itagaki",699.00,"Animal society drama.","https://comicvine.gamespot.com/a/uploads/scale_large/11139/111391600/8970681-0baf1a4a0e0e4261808f58d664b1e958.jpg",List.of(seinen, drama, psychological),null,null));
        books.add(new Book(null,"To Your Eternity","Yoshitoki Oima",689.00,"Immortal being journey.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9844008-wwww.jpg",List.of(fantasy, drama, adventure),null,null));
        books.add(new Book(null,"Wistoria: Wand and Sword","Yuzuru Tachikawa",629.00,"Afterlife judgment games.","https://comicvine.gamespot.com/a/uploads/scale_large/11161/111610434/8979589-1581855483-618VU.jpg",List.of(psychological, supernatural, thriller),null,null));

        books.add(new Book(null,"Frieren Beyond Journey's End","Kanehito Yamada",699.00,"Elf mage reflects after hero journey.","https://comicvine.gamespot.com/a/uploads/scale_large/11143/111435323/7595403-frieren.jpg",List.of(fantasy, adventure, sliceOfLife),null,null));
        books.add(new Book(null,"Undead Unluck","Yoshifumi Tozuka",629.00,"Unlucky girl meets undead fighter.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9897163-1974755851.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Call of the Night","Kotoyama",649.00,"Boy meets vampire at night.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9959274-0_000.jpg",List.of(romance, supernatural, shonen),null,null));
        books.add(new Book(null,"Oshi no Ko","Aka Akasaka",689.00,"Dark idol industry drama.","https://comicvine.gamespot.com/a/uploads/scale_large/11135/111359134/8399482-oshinoko%237-page1.jpg",List.of(drama, mystery, seinen),null,null));
        books.add(new Book(null,"Mashle Magic and Muscles","Hajime Komoto",619.00,"Magic school muscle comedy.","https://comicvine.gamespot.com/a/uploads/scale_large/11161/111610434/8802380-3935840979-97840.jpg",List.of(comedy, fantasy, school),null,null));

        books.add(new Book(null,"Kaiju No 8","Naoya Matsumoto",669.00,"Man transforms into kaiju.","https://comicvine.gamespot.com/a/uploads/scale_large/11/110017/9746089-w.jpg",List.of(action, sciFi, shonen),null,null));
        books.add(new Book(null,"Insomniacs After School","Makoto Ojiro",599.00,"Students bond over insomnia.","https://comicvine.gamespot.com/a/uploads/scale_large/11139/111391600/8815109-3c571967-1286-4d54-8ab8-2678532963bd.jpeg",List.of(romance, sliceOfLife, school),null,null));
        books.add(new Book(null,"Dead Mount Death Play","Ryohgo Narita",659.00,"Necromancer reborn modern Tokyo.","https://comicvine.gamespot.com/a/uploads/scale_large/6/67663/6395985-01.jpg",List.of(isekai, supernatural, action),null,null));
        books.add(new Book(null,"Skip and Loafer","Misaki Takamatsu",609.00,"Country girl city school life.","https://comicvine.gamespot.com/a/uploads/scale_large/11161/111610434/8731775-4440272168-61qI3.jpg",List.of(sliceOfLife, school, seinen),null,null));
        books.add(new Book(null,"Akane Banashi","Yuki Suenaga",629.00,"Rakugo performance story.","https://comicvine.gamespot.com/a/uploads/scale_large/11157/111574229/8514808-ftb4xemwuaiwhdn.jpg",List.of(drama, shonen, sliceOfLife),null,null)); // Hajime no Ippo

        books.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        bookRepository.saveAll(books);
    }
}