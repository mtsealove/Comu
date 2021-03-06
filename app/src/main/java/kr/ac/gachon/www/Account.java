package kr.ac.gachon.www;

public class Account {
    static int current; //현재 ID의 순번을 저장할 인덱스
    static final int non_member=100; //비회원 인덱스
    static final int manager=non_member+1; //관리자 인덱스
    static int count; //저장된 계정의 개수
    String name;
    String ID;
    String phone;
    String password;
    int level; //레벨을 정수로 저장
    int exp;
    Item[] items=null; //사용자가 소유하고 있는 아이템
    String[] finished_chapeter=new String[100]; //진행사항 저장
    String fc; //일렬로 진행사항을 받아옴
    String[] friends=new String [100];
    String friends_split;

    Account(String name, String ID, String phone, String password, int level, int exp, String fc, String friends_split) {
        this.name=name;
        this.ID=ID;
        this.phone=phone;
        this.password=password;
        this.level=level;
        this.exp=exp;
        this.fc=fc;
        finished_chapeter=fc.split(","); //진행사항을 개별로 분배
        this.friends_split=friends_split;
        friends=friends_split.split(","); //친구목록
    }

    void re_split_chapter() {
        finished_chapeter=fc.split(",");
    }
}
class Item {
    String category;
    int cost;
    String name;
}
