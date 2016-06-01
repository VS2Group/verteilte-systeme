package de.hska.lkit.demo.web.model.timeline;

import de.hska.lkit.demo.web.persistency.Persistency;
import de.hska.lkit.demo.web.model.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.hska.lkit.demo.web.model.user.User;

import java.util.List;

/**
 * Created by patrickkoenig on 01.06.16.
 */

@Service
public class TimelineLogic {

    public static final int POSTS_PER_PAGE_COUNT = 10;

    private final Persistency persistency;

    @Autowired
    public TimelineLogic (Persistency persistency) {
        this.persistency = persistency;
    }

    public Timeline createGlobalTimelineForRange(long start, long end) {
        Timeline timeline = new Timeline();
        List<Post> posts = persistency.findGlobalPosts(start, end);

        for (int i = posts.size() - 1; i >= 0; i--) {
            Post post = posts.get(i);
            String postId = Integer.toString(post.getId());
            User userForPost = persistency.findUserForPost(postId);
            timeline.getEntries().put(posts.get(i), userForPost);
        }
        return timeline;
    }
}
