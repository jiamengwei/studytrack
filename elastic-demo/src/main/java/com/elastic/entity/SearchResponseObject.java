package com.elastic.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class SearchResponseObject {

    private int took;
    private boolean timed_out;
    private ShardsBean _shards;
    private HitsBeanX hits;

    @NoArgsConstructor
    @Data
    public static class ShardsBean {
        private int total;
        private int successful;
        private int skipped;
        private int failed;
    }

    @NoArgsConstructor
    @Data
    public static class HitsBeanX {
        private int total;
        private double max_score;
        private List<HitsBean> hits;

        @NoArgsConstructor
        @Data
        public static class HitsBean {
            private String _index;
            private String _type;
            private String _id;
            private int _version;
            private double _score;
            private SourceBean _source;
            private HighlightBean highlight;

            @NoArgsConstructor
            @Data
            public static class SourceBean {
                private int id;
                private String projectName;
                private int projectManagerId;
                private String projectRelatedUser;
                private boolean projectStatus;
                private String startTime;
                private int companyId;
                private String address;
                private double longitude;
                private double latitude;
                private String firstPartyName;
                private String firstPartyTel;
                private Object projectDescription;
                private Object count;
                private String tid;
                private String isReport;
                private String isGroupChat;
            }

            @NoArgsConstructor
            @Data
            public static class HighlightBean {
                private List<String> projectName;
            }
        }
    }
}
