package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov"),
            new User(6, "pashka", "Pavel Mavrin"),
            new User(9, "geranazavr555", "Georgiy Nazarov"),
            new User(11, "tourist", "Gennady Korotkevich")
    );
    private static final List<Post> POSTS = Arrays.asList(
            new Post(5, "Fifth", "A New Russian is riding a steep Mercedes and then a simple engineer crashes into him on an old Muscovite. HP jumps out of the car, pulls out the poor puny engineer and says that he is now on the spot and will solve it. The engineer is so scared and says that he is an engineer and can fix everything, he just needs to see what he has done. They approached the merc, the engineer looked at it and said that if you blow hard into the exhaust pipe, all the bruises will gradually straighten out, but he (the engineer) is puny, weak and he does not have enough strength to blow.\n" +
                    "There's nothing to do, HP lay down and blows his mercedes into the exhaust pipe, and the engineer quietly left. NR is lying tensing up, and then the brothers were passing by, saw this case and stopped:\n" +
                    "- Hello, brother! What are you doing under the car, really? HP explained the situation, the brothers nodded understandingly and began to walk around the merc with intelligent faces, and then one of them said:\n" +
                    "- You're not going to make it, you didn't close the hatch!", 1),

            new Post(4, "Third", "HihiHaha", 6),
            new Post(3, "Third", "During the service in the church, a heavy downpour began outside and did not stop for several hours in a row. anekdotov.net A river overflowed its banks. Slowly, the church begins to flood. People are gradually leaving. The pastor remains in place. One of the parishioners says to the priest standing ankle-deep in the water: \"Father, let's save ourselves! \"Don't, God save me!\" The water continues to rise. The priest is already knee-deep in the water. A truck pulls up, a guy leans out of it: \"Hey, Dad! Come on, get in the car, let's save ourselves! - Don't, God save me! \"Well, as you wish, Father.\" The water is coming. It's already up to the pastor's chest. A boat comes up, a guy looks out of it and says, \"Dad, let's get in the boat. Save yourself! - Don't, God save me! \"Well, see for yourself.\".. The water is already reaching the priest's throat. A helicopter arrives, a guy leans out of it and shouts, throwing off a rope ladder: \"Father, get in here! Save yourself! \"Don't. God save me!\" And a wave swept over the pastor. And he drowned. I woke up in paradise. He immediately runs to God and shouts, \"Why didn't you save me? I was counting on you so much!!!\" And God answered him: \"Listen, I sent you a man, a truck, a boat, a helicopter. What else did you need?!\"", 1),
            new Post(2, "Second", "I'm just sleeping", 11),
            new Post(1, "First", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!!!!!!!!!!!", 1)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
