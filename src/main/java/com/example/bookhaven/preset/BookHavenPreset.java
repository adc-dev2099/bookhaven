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

        // ── Categories ─────────────────────────────────────
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(null, "Dark Fantasy", null));
        categories.add(new Category(null, "Adventure", null));
        categories.add(new Category(null, "Comedy", null));
        categories.add(new Category(null, "Superhero", null));
        categories.add(new Category(null, "Sports", null));

        categoryRepository.saveAll(categories);

        Category darkFantasy = categories.get(0);
        Category adventure   = categories.get(1);
        Category comedy      = categories.get(2);
        Category superhero   = categories.get(3);
        Category sports      = categories.get(4);

        // ── Books ──────────────────────────────────────────
        List<Book> books = new ArrayList<>();

        // Dark Fantasy
        books.add(new Book(null,"Attack on Titan","Hajime Isayama",729.00,"Humanity lives inside cities surrounded by enormous walls due to the Titans.","https://covers.openlibrary.org/b/isbn/9781612620244-L.jpg",List.of(darkFantasy),null,null));
        books.add(new Book(null,"Berserk","Kentaro Miura",899.00,"Guts, a lone mercenary warrior, battles demonic forces in a dark medieval world.","https://covers.openlibrary.org/b/isbn/9781593070205-L.jpg",List.of(darkFantasy),null,null));
        books.add(new Book(null,"Claymore","Norihiro Yagi",649.00,"Half-human half-demon warriors fight shape-shifting monsters.","https://covers.openlibrary.org/b/isbn/9781421506180-L.jpg",List.of(darkFantasy),null,null));
        books.add(new Book(null,"Vinland Saga","Makoto Yukimura",749.00,"Young Thorfinn seeks revenge in Viking era Europe.","https://covers.openlibrary.org/b/isbn/9781612624204-L.jpg",List.of(darkFantasy),null,null));
        books.add(new Book(null,"Chainsaw Man","Tatsuki Fujimoto",599.00,"Denji merges with a devil to become Chainsaw Man.","https://covers.openlibrary.org/b/isbn/9781974709939-L.jpg",List.of(darkFantasy),null,null));
        books.add(new Book(null,"Demon Slayer","Koyoharu Gotouge",619.00,"Tanjiro joins Demon Slayer Corps to save his sister.","https://covers.openlibrary.org/b/isbn/9781974700523-L.jpg",List.of(darkFantasy),null,null));

        // Adventure
        books.add(new Book(null,"One Piece","Eiichiro Oda",679.00,"Luffy searches for the One Piece treasure.","https://covers.openlibrary.org/b/isbn/9781569319017-L.jpg",List.of(adventure),null,null));
        books.add(new Book(null,"Dragon Ball","Akira Toriyama",649.00,"Goku searches for Dragon Balls.","https://covers.openlibrary.org/b/isbn/9781569319208-L.jpg",List.of(adventure),null,null));
        books.add(new Book(null,"Fullmetal Alchemist","Hiromu Arakawa",719.00,"Two brothers use alchemy to restore bodies.","https://covers.openlibrary.org/b/isbn/9781591169208-L.jpg",List.of(adventure),null,null));
        books.add(new Book(null,"Hunter x Hunter","Yoshihiro Togashi",689.00,"Gon becomes a Hunter.","https://covers.openlibrary.org/b/isbn/9781591167532-L.jpg",List.of(adventure),null,null));
        books.add(new Book(null,"Fairy Tail","Hiro Mashima",629.00,"Lucy joins Fairy Tail guild.","https://covers.openlibrary.org/b/isbn/9780345501332-L.jpg",List.of(adventure),null,null));
        books.add(new Book(null,"Black Clover","Yuki Tabata",599.00,"Asta aims to become Wizard King.","https://covers.openlibrary.org/b/isbn/9781421587189-L.jpg",List.of(adventure),null,null));

        // Comedy
        books.add(new Book(null,"Naruto","Masashi Kishimoto",619.00,"Naruto dreams of becoming Hokage.","https://covers.openlibrary.org/b/isbn/9781569319000-L.jpg",List.of(comedy),null,null));
        books.add(new Book(null,"Assassination Classroom","Yusei Matsui",589.00,"Students try to assassinate teacher.","https://covers.openlibrary.org/b/isbn/9781421576077-L.jpg",List.of(comedy),null,null));
        books.add(new Book(null,"Gintama","Hideaki Sorachi",649.00,"Comedy samurai parody.","https://covers.openlibrary.org/b/isbn/9781421506340-L.jpg",List.of(comedy),null,null));
        books.add(new Book(null,"Ouran Host Club","Bisco Hatori",569.00,"Host club comedy.","https://covers.openlibrary.org/b/isbn/9781421500577-L.jpg",List.of(comedy),null,null));
        books.add(new Book(null,"Saiki K","Shuichi Aso",549.00,"Psychic comedy.","https://covers.openlibrary.org/b/isbn/9781974700240-L.jpg",List.of(comedy),null,null));
        books.add(new Book(null,"KonoSuba","Natsume Akatsuki",579.00,"Fantasy parody comedy.","https://covers.openlibrary.org/b/isbn/9780316468701-L.jpg",List.of(comedy),null,null));

        // Superhero
        books.add(new Book(null,"My Hero Academia","Kohei Horikoshi",649.00,"Hero academy story.","https://covers.openlibrary.org/b/isbn/9781421582696-L.jpg",List.of(superhero),null,null));
        books.add(new Book(null,"One Punch Man","ONE",699.00,"Hero defeats enemies in one punch.","https://covers.openlibrary.org/b/isbn/9781421585642-L.jpg",List.of(superhero),null,null));
        books.add(new Book(null,"Mob Psycho 100","ONE",629.00,"Powerful esper teen.","https://covers.openlibrary.org/b/isbn/9781506721576-L.jpg",List.of(superhero),null,null));
        books.add(new Book(null,"Tiger & Bunny","Mizuki Sakakibara",589.00,"Corporate superheroes.","https://covers.openlibrary.org/b/isbn/9781421554471-L.jpg",List.of(superhero),null,null));
        books.add(new Book(null,"Vigilante MHA","Hideyuki Furuhashi",569.00,"Unlicensed heroes.","https://covers.openlibrary.org/b/isbn/9781974700196-L.jpg",List.of(superhero),null,null));
        books.add(new Book(null,"Ratman","Inui Sekihiko",549.00,"Hero vs villain comedy.","https://covers.openlibrary.org/b/isbn/9781427816153-L.jpg",List.of(superhero),null,null));

        // Sports
        books.add(new Book(null,"Blue Lock","Muneyuki Kaneshiro",599.00,"Soccer battle royale.","https://covers.openlibrary.org/b/isbn/9781646512942-L.jpg",List.of(sports),null,null));
        books.add(new Book(null,"Haikyuu!!","Haruichi Furudate",649.00,"Volleyball team story.","https://covers.openlibrary.org/b/isbn/9781421587660-L.jpg",List.of(sports),null,null));
        books.add(new Book(null,"Slam Dunk","Takehiko Inoue",679.00,"Basketball classic.","https://covers.openlibrary.org/b/isbn/9781591161462-L.jpg",List.of(sports),null,null));
        books.add(new Book(null,"Kuroko Basketball","Tadatoshi Fujimaki",629.00,"Generation of Miracles.","https://covers.openlibrary.org/b/isbn/9781421566412-L.jpg",List.of(sports),null,null));
        books.add(new Book(null,"Eyeshield 21","Riichiro Inagaki",589.00,"American football anime.","https://covers.openlibrary.org/b/isbn/9781591167983-L.jpg",List.of(sports),null,null));
        books.add(new Book(null,"Ping Pong","Taiyou Matsumoto",569.00,"Competitive table tennis.","https://covers.openlibrary.org/b/isbn/9781683960850-L.jpg",List.of(sports),null,null));

        bookRepository.saveAll(books);
    }
}