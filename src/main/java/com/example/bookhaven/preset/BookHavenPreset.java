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

    @PostConstruct
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
        books.add(new Book(null,"Jujutsu Kaisen","Gege Akutami",689.00,"Sorcerers battle curses.","https://covers.openlibrary.org/b/isbn/9781974710027-L.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Vinland Saga","Makoto Yukimura",749.00,"Viking revenge journey.","https://covers.openlibrary.org/b/isbn/9781612624204-L.jpg",List.of(action, historical, seinen),null,null));
        books.add(new Book(null,"Kaguya Sama Love is War","Aka Akasaka",629.00,"Romantic mind games.","https://covers.openlibrary.org/b/isbn/9781421591063-L.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Blue Lock","Muneyuki Kaneshiro",599.00,"Soccer survival battle.","https://covers.openlibrary.org/b/isbn/9781646512942-L.jpg",List.of(sports, psychological, shonen),null,null));
        books.add(new Book(null,"Made in Abyss","Akihito Tsukushi",719.00,"Abyss exploration.","https://covers.openlibrary.org/b/isbn/9781626927735-L.jpg",List.of(adventure, fantasy, mystery),null,null));

        books.add(new Book(null,"Fruits Basket","Natsuki Takaya",619.00,"Zodiac family curse.","https://covers.openlibrary.org/b/isbn/9781427813862-L.jpg",List.of(romance, supernatural, shojo),null,null));
        books.add(new Book(null,"Akira","Katsuhiro Otomo",899.00,"Cyberpunk rebellion.","https://covers.openlibrary.org/b/isbn/9781935429005-L.jpg",List.of(sciFi, action, seinen),null,null));
        books.add(new Book(null,"Barakamon","Satsuki Yoshino",599.00,"Countryside life.","https://covers.openlibrary.org/b/isbn/9780316336086-L.jpg",List.of(sliceOfLife, comedy, seinen),null,null));
        books.add(new Book(null,"Parasyte","Hitoshi Iwaaki",679.00,"Alien parasites invade.","https://covers.openlibrary.org/b/isbn/9781612620336-L.jpg",List.of(horror, psychological, sciFi),null,null));
        books.add(new Book(null,"Haikyuu","Haruichi Furudate",649.00,"Volleyball team story.","https://covers.openlibrary.org/b/isbn/9781421587660-L.jpg",List.of(sports, school, shonen),null,null));

        books.add(new Book(null,"Noragami","Adachitoka",619.00,"Stray god adventures.","https://covers.openlibrary.org/b/isbn/9781612629063-L.jpg",List.of(supernatural, action, shonen),null,null));
        books.add(new Book(null,"Steins Gate","5pb",699.00,"Time travel thriller.","https://covers.openlibrary.org/b/isbn/9781927925850-L.jpg",List.of(sciFi, thriller, psychological),null,null));
        books.add(new Book(null,"Orange","Ichigo Takano",619.00,"Letters from future.","https://covers.openlibrary.org/b/isbn/9781626923027-L.jpg",List.of(drama, romance, school),null,null));
        books.add(new Book(null,"Gantz","Hiroya Oku",729.00,"Deadly alien missions.","https://covers.openlibrary.org/b/isbn/9781591164029-L.jpg",List.of(action, sciFi, seinen),null,null));
        books.add(new Book(null,"Yotsuba","Kiyohiko Azuma",589.00,"Child daily adventures.","https://covers.openlibrary.org/b/isbn/9780316073875-L.jpg",List.of(sliceOfLife, comedy),null,null));

        books.add(new Book(null,"Erased","Kei Sanbe",629.00,"Time travel mystery.","https://covers.openlibrary.org/b/isbn/9780316387842-L.jpg",List.of(mystery, thriller, supernatural),null,null));
        books.add(new Book(null,"Blue Period","Tsubasa Yamaguchi",699.00,"Art student growth.","https://covers.openlibrary.org/b/isbn/9781632368782-L.jpg",List.of(drama, school, seinen),null,null));
        books.add(new Book(null,"Re Zero","Tappei Nagatsuki",649.00,"Death reset world.","https://covers.openlibrary.org/b/isbn/9780316315319-L.jpg",List.of(isekai, fantasy, psychological),null,null));
        books.add(new Book(null,"Nana","Ai Yazawa",649.00,"Two girls same name.","https://covers.openlibrary.org/b/isbn/9781421501086-L.jpg",List.of(josei, romance, drama),null,null));
        books.add(new Book(null,"Evangelion","Yoshiyuki Sadamoto",749.00,"Teens pilot robots.","https://covers.openlibrary.org/b/isbn/9781421550794-L.jpg",List.of(mecha, psychological, sciFi),null,null));

        books.add(new Book(null,"Detective Conan","Gosho Aoyama",679.00,"Child detective.","https://covers.openlibrary.org/b/isbn/9781421501666-L.jpg",List.of(mystery, shonen),null,null));
        books.add(new Book(null,"Beck","Harold Sakuishi",649.00,"Rock band journey.","https://covers.openlibrary.org/b/isbn/9781595328700-L.jpg",List.of(music, sliceOfLife, seinen),null,null));
        books.add(new Book(null,"Kimi ni Todoke","Karuho Shiina",619.00,"Shy girl love.","https://covers.openlibrary.org/b/isbn/9781421527550-L.jpg",List.of(romance, school, shojo),null,null));
        books.add(new Book(null,"Blame","Tsutomu Nihei",749.00,"Mega structure future.","https://covers.openlibrary.org/b/isbn/9781945054976-L.jpg",List.of(sciFi, action, mystery),null,null));
        books.add(new Book(null,"Golden Kamuy","Satoru Noda",689.00,"Treasure hunt Hokkaido.","https://covers.openlibrary.org/b/isbn/9781421594880-L.jpg",List.of(historical, adventure, seinen),null,null));

        // Multi genre books 2
        books.add(new Book(null,"Dorohedoro","Q Hayashida",699.00,"Amnesiac hunts sorcerers.","https://covers.openlibrary.org/b/isbn/9781421561325-L.jpg",List.of(action, fantasy, seinen),null,null));
        books.add(new Book(null,"Toradora","Yuyuko Takemiya",599.00,"Unexpected school romance.","https://covers.openlibrary.org/b/isbn/9781626927957-L.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Ajin","Gamon Sakurai",689.00,"Immortal humans hunted.","https://covers.openlibrary.org/b/isbn/9781942993163-L.jpg",List.of(action, horror, psychological),null,null));
        books.add(new Book(null,"Planetes","Makoto Yukimura",689.00,"Space debris workers.","https://covers.openlibrary.org/b/isbn/9781593073343-L.jpg",List.of(sciFi, sliceOfLife, seinen),null,null));
        books.add(new Book(null,"Chihayafuru","Yuki Suetsugu",629.00,"Competitive karuta.","https://covers.openlibrary.org/b/isbn/9781632369666-L.jpg",List.of(josei, sports, drama),null,null));

        books.add(new Book(null,"Fire Force","Atsushi Ohkubo",599.00,"Fire soldiers battle infernals.","https://covers.openlibrary.org/b/isbn/9781632363305-L.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Grand Blue","Kenji Inoue",599.00,"College diving comedy.","https://covers.openlibrary.org/b/isbn/9781632364975-L.jpg",List.of(comedy, sliceOfLife, seinen),null,null));
        books.add(new Book(null,"Overlord","Kugane Maruyama",699.00,"Game world overlord.","https://covers.openlibrary.org/b/isbn/9780316363914-L.jpg",List.of(isekai, fantasy, action),null,null));
        books.add(new Book(null,"Lovely Complex","Aya Nakahara",589.00,"Height difference romance.","https://covers.openlibrary.org/b/isbn/9781421500812-L.jpg",List.of(romance, comedy, shojo),null,null));
        books.add(new Book(null,"Mob Psycho 100","ONE",629.00,"Powerful esper teen.","https://covers.openlibrary.org/b/isbn/9781506721576-L.jpg",List.of(supernatural, comedy, action),null,null));

        books.add(new Book(null,"Blue Exorcist","Kazue Kato",629.00,"Demon exorcists.","https://covers.openlibrary.org/b/isbn/9781421540320-L.jpg",List.of(supernatural, action, shonen),null,null));
        books.add(new Book(null,"Witch Hat Atelier","Kamome Shirahama",689.00,"Magic apprentice.","https://covers.openlibrary.org/b/isbn/9781632367709-L.jpg",List.of(fantasy, adventure, seinen),null,null));
        books.add(new Book(null,"My Love Story","Kazune Kawahara",589.00,"Sweet romance.","https://covers.openlibrary.org/b/isbn/9781421587677-L.jpg",List.of(romance, comedy, shojo),null,null));
        books.add(new Book(null,"Akame ga Kill","Takahiro",619.00,"Assassins fight empire.","https://covers.openlibrary.org/b/isbn/9780316258784-L.jpg",List.of(action, fantasy, shonen),null,null));
        books.add(new Book(null,"Dr Stone","Riichiro Inagaki",599.00,"Science rebuilds world.","https://covers.openlibrary.org/b/isbn/9781974702619-L.jpg",List.of(adventure, sciFi, shonen),null,null));

        books.add(new Book(null,"Horimiya","HERO",629.00,"Hidden personalities.","https://covers.openlibrary.org/b/isbn/9780316342032-L.jpg",List.of(romance, sliceOfLife, school),null,null));
        books.add(new Book(null,"Hinamatsuri","Masao Ohtake",609.00,"Psychic girl comedy.","https://covers.openlibrary.org/b/isbn/9780316556830-L.jpg",List.of(comedy, supernatural, seinen),null,null));
        books.add(new Book(null,"Kingdom","Yasuhisa Hara",799.00,"Ancient China war.","https://covers.openlibrary.org/b/isbn/9781421594903-L.jpg",List.of(historical, action, seinen),null,null));
        books.add(new Book(null,"Yamada Kun and Seven Witches","Miki Yoshikawa",599.00,"Body swap school.","https://covers.openlibrary.org/b/isbn/9781632361714-L.jpg",List.of(school, supernatural, comedy),null,null));
        books.add(new Book(null,"Battle Angel Alita","Yukito Kishiro",699.00,"Cyborg warrior.","https://covers.openlibrary.org/b/isbn/9781632367112-L.jpg",List.of(sciFi, action, seinen),null,null));

        books.add(new Book(null,"Nichijou","Keiichi Arawi",569.00,"Absurd school comedy.","https://covers.openlibrary.org/b/isbn/9781935429005-L.jpg",List.of(comedy, school, sliceOfLife),null,null));
        books.add(new Book(null,"Radiant","Tony Valente",649.00,"Wizard adventure.","https://covers.openlibrary.org/b/isbn/9781421593364-L.jpg",List.of(adventure, fantasy, shonen),null,null));
        books.add(new Book(null,"Paradise Kiss","Ai Yazawa",679.00,"Fashion romance.","https://covers.openlibrary.org/b/isbn/9781421501086-L.jpg",List.of(josei, romance, drama),null,null));
        books.add(new Book(null,"Future Diary","Sakae Esuno",629.00,"Deadly diary game.","https://covers.openlibrary.org/b/isbn/9780316213294-L.jpg",List.of(thriller, psychological, supernatural),null,null));
        books.add(new Book(null,"Non Non Biyori","Atto",569.00,"Countryside school life.","https://covers.openlibrary.org/b/isbn/9781626920439-L.jpg",List.of(sliceOfLife, comedy, school),null,null));

        // Multi genre books 3
        books.add(new Book(null,"Claymore","Norihiro Yagi",649.00,"Half-human demon hunters.","https://covers.openlibrary.org/b/isbn/9781421506180-L.jpg",List.of(action, fantasy, shonen),null,null));
        books.add(new Book(null,"Skip Beat","Yoshiki Nakamura",629.00,"Showbiz revenge story.","https://covers.openlibrary.org/b/isbn/9781421505879-L.jpg",List.of(shojo, drama, romance),null,null));
        books.add(new Book(null,"Uzumaki","Junji Ito",699.00,"Town cursed by spirals.","https://covers.openlibrary.org/b/isbn/9781421561325-L.jpg",List.of(horror, mystery, supernatural),null,null));
        books.add(new Book(null,"Delicious in Dungeon","Ryoko Kui",659.00,"Dungeon cooking adventure.","https://covers.openlibrary.org/b/isbn/9780316473064-L.jpg",List.of(fantasy, adventure, comedy),null,null));
        books.add(new Book(null,"Baby Steps","Hikaru Katsuki",619.00,"Tennis improvement journey.","https://covers.openlibrary.org/b/isbn/9781632362926-L.jpg",List.of(sports, school, shonen),null,null));

        books.add(new Book(null,"Pluto","Naoki Urasawa",719.00,"Robot murder mystery.","https://covers.openlibrary.org/b/isbn/9781421519180-L.jpg",List.of(mystery, sciFi, seinen),null,null));
        books.add(new Book(null,"Kuragehime","Akiko Higashimura",599.00,"Otaku girls life.","https://covers.openlibrary.org/b/isbn/9781612625959-L.jpg",List.of(josei, comedy, sliceOfLife),null,null));
        books.add(new Book(null,"Inuyasha","Rumiko Takahashi",679.00,"Feudal demon battles.","https://covers.openlibrary.org/b/isbn/9781421500942-L.jpg",List.of(action, supernatural, adventure),null,null));
        books.add(new Book(null,"Goodnight Punpun","Inio Asano",699.00,"Dark coming of age.","https://covers.openlibrary.org/b/isbn/9781421586200-L.jpg",List.of(seinen, psychological, drama),null,null));
        books.add(new Book(null,"K-On","Kakifly",599.00,"School band life.","https://covers.openlibrary.org/b/isbn/9780316177658-L.jpg",List.of(music, comedy, sliceOfLife),null,null));

        books.add(new Book(null,"Toriko","Mitsutoshi Shimabukuro",589.00,"Gourmet hunter adventure.","https://covers.openlibrary.org/b/isbn/9781421539812-L.jpg",List.of(adventure, action, shonen),null,null));
        books.add(new Book(null,"Snow White with the Red Hair","Sorata Akizuki",599.00,"Fantasy romance.","https://covers.openlibrary.org/b/isbn/9781421587813-L.jpg",List.of(shojo, fantasy, romance),null,null));
        books.add(new Book(null,"Ghost in the Shell","Masamune Shirow",799.00,"Cyber police.","https://covers.openlibrary.org/b/isbn/9781569319017-L.jpg",List.of(sciFi, action, seinen),null,null));
        books.add(new Book(null,"Tomie","Junji Ito",679.00,"Immortal terrifying girl.","https://covers.openlibrary.org/b/isbn/9781421590561-L.jpg",List.of(horror, supernatural, mystery),null,null));
        books.add(new Book(null,"Flying Witch","Chihiro Ishizuka",579.00,"Relaxing witch life.","https://covers.openlibrary.org/b/isbn/9781942993163-L.jpg",List.of(sliceOfLife, supernatural, comedy),null,null));

        books.add(new Book(null,"Slam Dunk","Takehiko Inoue",679.00,"Basketball classic.","https://covers.openlibrary.org/b/isbn/9781591161462-L.jpg",List.of(sports, school, shonen),null,null));
        books.add(new Book(null,"Otoyomegatari","Kaoru Mori",719.00,"Central Asia brides.","https://covers.openlibrary.org/b/isbn/9780316180993-L.jpg",List.of(historical, romance, seinen),null,null));
        books.add(new Book(null,"No Game No Life","Yuu Kamiya",619.00,"Gamers rule world.","https://covers.openlibrary.org/b/isbn/9780316383110-L.jpg",List.of(isekai, fantasy, comedy),null,null));
        books.add(new Book(null,"Homunculus","Hideo Yamamoto",699.00,"Trepanation mind horror.","https://covers.openlibrary.org/b/isbn/9781421529318-L.jpg",List.of(psychological, seinen, thriller),null,null));
        books.add(new Book(null,"Given","Natsuki Kizu",629.00,"Band romance drama.","https://covers.openlibrary.org/b/isbn/9781974711826-L.jpg",List.of(music, romance, drama),null,null));

        books.add(new Book(null,"Kemono Jihen","Sho Aimoto",609.00,"Supernatural detective kids.","https://covers.openlibrary.org/b/isbn/9781974708932-L.jpg",List.of(supernatural, mystery, shonen),null,null));
        books.add(new Book(null,"Solanin","Inio Asano",649.00,"Young adult life.","https://covers.openlibrary.org/b/isbn/9781421527666-L.jpg",List.of(drama, sliceOfLife, seinen),null,null));
        books.add(new Book(null,"So I'm a Spider So What","Okina Baba",639.00,"Reborn as spider.","https://covers.openlibrary.org/b/isbn/9780316412895-L.jpg",List.of(isekai, fantasy, adventure),null,null));
        books.add(new Book(null,"20th Century Boys","Naoki Urasawa",759.00,"Cult conspiracy thriller.","https://covers.openlibrary.org/b/isbn/9781421535401-L.jpg",List.of(thriller, mystery, seinen),null,null));
        books.add(new Book(null,"Yowamushi Pedal","Wataru Watanabe",599.00,"Cycling competition.","https://covers.openlibrary.org/b/isbn/9780316258777-L.jpg",List.of(sports, school, shonen),null,null));

        // Multi genre books 4
        books.add(new Book(null,"Hell's Paradise","Yuji Kaku",649.00,"Ninja survival island.","https://covers.openlibrary.org/b/isbn/9781974713202-L.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Honey and Clover","Chica Umino",619.00,"College romance.","https://covers.openlibrary.org/b/isbn/9781421500843-L.jpg",List.of(josei, romance, sliceOfLife),null,null));
        books.add(new Book(null,"Ajin Demi Human","Gamon Sakurai",689.00,"Immortal hunted humans.","https://covers.openlibrary.org/b/isbn/9781942993163-L.jpg",List.of(action, horror, seinen),null,null));
        books.add(new Book(null,"Sket Dance","Kenta Shinohara",579.00,"School helpers club.","https://covers.openlibrary.org/b/isbn/9781421537771-L.jpg",List.of(comedy, school, shonen),null,null));
        books.add(new Book(null,"The Faraway Paladin","Kanata Yanagino",629.00,"Raised by undead.","https://covers.openlibrary.org/b/isbn/9781718371705-L.jpg",List.of(fantasy, adventure, isekai),null,null));

        books.add(new Book(null,"Sun Ken Rock","Boichi",689.00,"Gang leader rise.","https://covers.openlibrary.org/b/isbn/9781939130143-L.jpg",List.of(seinen, action, drama),null,null));
        books.add(new Book(null,"Tsurezure Children","Toshiya Wakabayashi",569.00,"Multiple couples.","https://covers.openlibrary.org/b/isbn/9781632365057-L.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Talentless Nana","Looseboy",599.00,"Hidden killer school.","https://covers.openlibrary.org/b/isbn/9780316441901-L.jpg",List.of(mystery, thriller, school),null,null));
        books.add(new Book(null,"Blue Giant","Shinichi Ishizuka",689.00,"Jazz saxophonist dream.","https://covers.openlibrary.org/b/isbn/9781647292065-L.jpg",List.of(music, drama, seinen),null,null));
        books.add(new Book(null,"Innocent","Shinichi Sakamoto",699.00,"French revolution drama.","https://covers.openlibrary.org/b/isbn/9781421586439-L.jpg",List.of(historical, drama, seinen),null,null));

        books.add(new Book(null,"Alice in Borderland","Haro Aso",649.00,"Deadly game world.","https://covers.openlibrary.org/b/isbn/9781421594187-L.jpg",List.of(thriller, psychological, mystery),null,null));
        books.add(new Book(null,"Natsume Book of Friends","Yuki Midorikawa",599.00,"Boy sees spirits.","https://covers.openlibrary.org/b/isbn/9781421532431-L.jpg",List.of(supernatural, sliceOfLife, shojo),null,null));
        books.add(new Book(null,"D-Frag","Tomoya Haruno",559.00,"School game club.","https://covers.openlibrary.org/b/isbn/9781626920811-L.jpg",List.of(comedy, school, sliceOfLife),null,null));
        books.add(new Book(null,"Radiant Dream","Tony Valente",639.00,"Magic world adventure.","https://covers.openlibrary.org/b/isbn/9781421593364-L.jpg",List.of(fantasy, adventure, shonen),null,null));
        books.add(new Book(null,"Shiver","Junji Ito",689.00,"Short horror stories.","https://covers.openlibrary.org/b/isbn/9781421596938-L.jpg",List.of(horror, mystery, thriller),null,null));

        books.add(new Book(null,"Ping Pong","Taiyou Matsumoto",569.00,"Competitive table tennis.","https://covers.openlibrary.org/b/isbn/9781683960850-L.jpg",List.of(sports, drama, seinen),null,null));
        books.add(new Book(null,"Say I Love You","Kanae Hazuki",579.00,"School romance drama.","https://covers.openlibrary.org/b/isbn/9781612620244-L.jpg",List.of(romance, school, shojo),null,null));
        books.add(new Book(null,"Code Geass","Ichiro Okouchi",699.00,"Rebellion mechs.","https://covers.openlibrary.org/b/isbn/9781594099762-L.jpg",List.of(mecha, action, thriller),null,null));
        books.add(new Book(null,"Usagi Drop","Yumi Unita",629.00,"Single father story.","https://covers.openlibrary.org/b/isbn/9780316073905-L.jpg",List.of(sliceOfLife, drama, josei),null,null));
        books.add(new Book(null,"Btooom","Junya Inoue",659.00,"Island survival bombs.","https://covers.openlibrary.org/b/isbn/9780316213881-L.jpg",List.of(thriller, action, seinen),null,null));

        books.add(new Book(null,"Eyeshield 21","Riichiro Inagaki",589.00,"American football.","https://covers.openlibrary.org/b/isbn/9781591167983-L.jpg",List.of(sports, comedy, shonen),null,null));
        books.add(new Book(null,"Magi","Shinobu Ohtaka",629.00,"Dungeon adventures.","https://covers.openlibrary.org/b/isbn/9781421549590-L.jpg",List.of(adventure, fantasy, shonen),null,null));
        books.add(new Book(null,"PTSD Radio","Masaaki Nakayama",629.00,"Urban horror anthology.","https://covers.openlibrary.org/b/isbn/9781634429412-L.jpg",List.of(horror, psychological, mystery),null,null));
        books.add(new Book(null,"Nodame Cantabile","Tomoko Ninomiya",619.00,"Classical music romance.","https://covers.openlibrary.org/b/isbn/9781612620244-L.jpg",List.of(music, romance, josei),null,null));
        books.add(new Book(null,"Judge","Yoshiki Tonogai",589.00,"Masked death game.","https://covers.openlibrary.org/b/isbn/9780316213928-L.jpg",List.of(thriller, horror, mystery),null,null));

        // Multi genre books 5
        books.add(new Book(null,"Chainsaw Man","Tatsuki Fujimoto",599.00,"Devil hunter chaos.","https://covers.openlibrary.org/b/isbn/9781974709939-L.jpg",List.of(action, horror, shonen),null,null));
        books.add(new Book(null,"Toradora Portable","Yuyuko Takemiya",579.00,"Romantic school chaos.","https://covers.openlibrary.org/b/isbn/9781626927957-L.jpg",List.of(romance, comedy, school),null,null));
        books.add(new Book(null,"Vagabond","Takehiko Inoue",799.00,"Samurai philosophical journey.","https://covers.openlibrary.org/b/isbn/9781421520544-L.jpg",List.of(seinen, historical, action),null,null));
        books.add(new Book(null,"Kuroko Basketball","Tadatoshi Fujimaki",629.00,"Generation of miracles.","https://covers.openlibrary.org/b/isbn/9781421566412-L.jpg",List.of(sports, school, shonen),null,null));
        books.add(new Book(null,"The Promised Neverland","Kaiu Shirai",649.00,"Escape orphanage.","https://covers.openlibrary.org/b/isbn/9781421597126-L.jpg",List.of(mystery, thriller, shonen),null,null));

        books.add(new Book(null,"A Silent Voice","Yoshitoki Oima",599.00,"Bullying redemption.","https://covers.openlibrary.org/b/isbn/9781632360564-L.jpg",List.of(drama, romance, school),null,null));
        books.add(new Book(null,"Danganronpa","Spike Chunsoft",599.00,"Killing school mystery.","https://covers.openlibrary.org/b/isbn/9781616559281-L.jpg",List.of(thriller, mystery, school),null,null));
        books.add(new Book(null,"Gundam Origin","Yoshikazu Yasuhiko",799.00,"Robot war.","https://covers.openlibrary.org/b/isbn/9781935654872-L.jpg",List.of(mecha, sciFi, action),null,null));
        books.add(new Book(null,"Kuroneko","Tsugumi Ohba",589.00,"Supernatural slice life.","https://covers.openlibrary.org/b/isbn/9780316073875-L.jpg",List.of(supernatural, sliceOfLife, comedy),null,null));
        books.add(new Book(null,"Orange Future","Ichigo Takano",619.00,"Future romance drama.","https://covers.openlibrary.org/b/isbn/9781626923027-L.jpg",List.of(romance, drama, supernatural),null,null));

        books.add(new Book(null,"Akudama Drive","Kazutaka Kodaka",689.00,"Cyberpunk criminals.","https://covers.openlibrary.org/b/isbn/9781975312344-L.jpg",List.of(sciFi, action, thriller),null,null));
        books.add(new Book(null,"Horimiya Extra","HERO",609.00,"School romance life.","https://covers.openlibrary.org/b/isbn/9780316342032-L.jpg",List.of(romance, sliceOfLife, school),null,null));
        books.add(new Book(null,"Beastars","Paru Itagaki",699.00,"Animal society drama.","https://covers.openlibrary.org/b/isbn/9781974707980-L.jpg",List.of(seinen, drama, psychological),null,null));
        books.add(new Book(null,"To Your Eternity","Yoshitoki Oima",689.00,"Immortal being journey.","https://covers.openlibrary.org/b/isbn/9781632365712-L.jpg",List.of(fantasy, drama, adventure),null,null));
        books.add(new Book(null,"Death Parade","Yuzuru Tachikawa",629.00,"Afterlife judgment games.","https://covers.openlibrary.org/b/isbn/9780316270007-L.jpg",List.of(psychological, supernatural, thriller),null,null));

        books.add(new Book(null,"Frieren Beyond Journey's End","Kanehito Yamada",699.00,"Elf mage reflects after hero journey.","https://covers.openlibrary.org/b/isbn/9781974725762-L.jpg",List.of(fantasy, adventure, sliceOfLife),null,null));
        books.add(new Book(null,"Undead Unluck","Yoshifumi Tozuka",629.00,"Unlucky girl meets undead fighter.","https://covers.openlibrary.org/b/isbn/9781974723195-L.jpg",List.of(action, supernatural, shonen),null,null));
        books.add(new Book(null,"Call of the Night","Kotoyama",649.00,"Boy meets vampire at night.","https://covers.openlibrary.org/b/isbn/9781974720514-L.jpg",List.of(romance, supernatural, shonen),null,null));
        books.add(new Book(null,"Oshi no Ko","Aka Akasaka",689.00,"Dark idol industry drama.","https://covers.openlibrary.org/b/isbn/9781975363178-L.jpg",List.of(drama, mystery, seinen),null,null));
        books.add(new Book(null,"Mashle Magic and Muscles","Hajime Komoto",619.00,"Magic school muscle comedy.","https://covers.openlibrary.org/b/isbn/9781974723348-L.jpg",List.of(comedy, fantasy, school),null,null));

        books.add(new Book(null,"Kaiju No 8","Naoya Matsumoto",669.00,"Man transforms into kaiju.","https://covers.openlibrary.org/b/isbn/9781974725984-L.jpg",List.of(action, sciFi, shonen),null,null));
        books.add(new Book(null,"Insomniacs After School","Makoto Ojiro",599.00,"Students bond over insomnia.","https://covers.openlibrary.org/b/isbn/9781974732739-L.jpg",List.of(romance, sliceOfLife, school),null,null));
        books.add(new Book(null,"Dead Mount Death Play","Ryohgo Narita",659.00,"Necromancer reborn modern Tokyo.","https://covers.openlibrary.org/b/isbn/9781975339050-L.jpg",List.of(isekai, supernatural, action),null,null));
        books.add(new Book(null,"Skip and Loafer","Misaki Takamatsu",609.00,"Country girl city school life.","https://covers.openlibrary.org/b/isbn/9781646513772-L.jpg",List.of(sliceOfLife, school, seinen),null,null));
        books.add(new Book(null,"Akane Banashi","Yuki Suenaga",629.00,"Rakugo performance story.","https://covers.openlibrary.org/b/isbn/9781974736287-L.jpg",List.of(drama, shonen, sliceOfLife),null,null));

        bookRepository.saveAll(books);
    }
}