package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.util.List;

/**
 * Created by mike on 2017/11/2.
 */

public class HomePageBannerResponse {
    private List<PostResponse> post_list;

    public List<PostResponse> getPost_list() {
        return post_list;
    }

    public void setPost_list(List<PostResponse> post_list) {
        this.post_list = post_list;
    }

    public static class PostResponse {
        public String getPost_url() {
            return post_url;
        }

        public void setPost_url(String post_url) {
            this.post_url = post_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        String post_url;
        String name;
        String cover_url;
    }
}
