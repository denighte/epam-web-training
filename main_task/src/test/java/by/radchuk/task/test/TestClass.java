package by.radchuk.task.test;

import by.radchuk.task.model.User;
import com.google.gson.Gson;
import lombok.var;

public class TestClass {


    public static void main(String[] args) {
        Gson gson = new Gson();
        User user = User.builder().id(5).login("login").passwordHash("1111").level((byte)0).build();
        var json = gson.toJsonTree(user).getAsJsonObject();
        json.addProperty("property", "example");
        //System.out.println(json);
        User deserializedUser = gson.fromJson(json.toString(), User.class);
        System.out.println(deserializedUser);
    }
}
