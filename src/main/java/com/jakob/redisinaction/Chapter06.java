package com.jakob.redisinaction;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class Chapter06 {
    public static void main(String[] args) {
        new Chapter06().run();
    }

    public void run() {
        Jedis conn = new Jedis("localhost");
        conn.select(15);

        testAddUpdateContact(conn);
        testAddressBookAutocomplete(conn);
    }

    public void testAddUpdateContact(Jedis conn) {
        System.out.println("\n----- testAddUpdateContact -----");
        conn.del("recent:user");

        System.out.println("Let's add a few contacts...");
        for (int i = 0; i < 10; i++) {
            addUpdateContact(conn, "user", "contact-" + i / 3 + "-" + i);
        }
        System.out.println("Current recently contacted contacts");
        List<String> contacts = conn.lrange("recent:user", 0, -1);
        contacts.forEach(c -> System.out.println(" " + c));
        assert contacts.size() >= 10;
        System.out.println();

        System.out.println("Let's pull one of the older ones up to the front");
        addUpdateContact(conn, "user", "contact-1-4");
        System.out.println("Now the top3 contacts:");
        List<String> top3 = conn.lrange("recent:user", 0, 2);
        top3.forEach(c -> System.out.println(" " + c));
        assert "contact-1-4".equals(top3.get(0));
        System.out.println();

        System.out.println("Let's remove a contact");
        removeContact(conn, "user", "contact-2-6");
        Long size = conn.llen("recent:user");
        System.out.println("Now the contacts number: " + size);
        assert size == 9;

        System.out.println("Let's try autocomplete");
        List<String> al1 = fetchAutocompleteList(conn, "user", "c");
        System.out.println("All contact start with 'c' : ");
        al1.forEach(c -> System.out.println(" " + c));
        assert al1.size() == 9;
        List<String> al2 = fetchAutocompleteList(conn, "user", "contact-2");
        System.out.println("All contact start with 'contact-2': ");
        al2.forEach(c -> System.out.println(" " + c));
        assert al2.size() == 2;
        conn.del("recent:user");
        System.out.println();
    }

    public void testAddressBookAutocomplete(Jedis conn) {
        System.out.println("\n----- testAddressBookAutocomplete -----");
        conn.del("members:test");
        System.out.println("the start/end range of 'abc' is: ");
        System.out.println(Arrays.toString(findPrefixRange("abc")));
        System.out.println();

        System.out.println("Let's add some members to the guild");
        for (String name : new String[]{"jeff", "jenny", "jack", "jennifer"}) {
            joinGuild(conn, "test", name);
        }
        Set<String> members = conn.zrange("members:test", 0, -1);
        System.out.println(members);
        System.out.println();

        System.out.println("These are members start with 'je':");
        members = autoCompleteOnPrefix(conn, "test", "je");
        System.out.println(members);
        assert members.size() == 3;

        System.out.println("jenny just left to join another guild..");
        leaveGuild(conn, "test", "jenny");
        members = autoCompleteOnPrefix(conn, "test", "je");
        System.out.println(members);
        assert members.size() == 2;
        conn.del("members:test");
    }

    public void addUpdateContact(Jedis conn, String user, String contact) {
        Transaction trans = conn.multi();
        String acList = "recent:" + user;
        trans.lrem(acList, 0, contact);
        trans.lpush(acList, contact);
        trans.ltrim(acList, 0, 99);
        trans.exec();
    }

    public void removeContact(Jedis conn, String user, String contact) {
        conn.lrem("recent:" + user, 0, contact);
    }

    public List<String> fetchAutocompleteList(Jedis conn, String user, String prefix) {
        List<String> candidates = conn.lrange("recent:" + user, 0, -1);
        return candidates.stream().filter(s -> s.toLowerCase().startsWith(prefix)).collect(Collectors.toList());
    }

    private static final String VALID_CHARACTERS = "`abcdefghijklmnopqrstuvwxyz{";

    public String[] findPrefixRange(String prefix) {
        int index = VALID_CHARACTERS.indexOf(prefix.charAt(prefix.length() - 1));
        char c = VALID_CHARACTERS.charAt(index > 0 ? index - 1 : 0);
        String start = prefix.substring(0, prefix.length() - 1) + c + "{";
        String end = prefix + "{";
        return new String[]{start, end};
    }

    public void joinGuild(Jedis conn, String guild, String user) {
        conn.zadd("members:" + guild, 0, user);
    }

    public void leaveGuild(Jedis conn, String guild, String user) {
        conn.zrem("members:" + guild, user);
    }

    public Set<String> autoCompleteOnPrefix(Jedis conn, String guild, String prefix) {
        String[] range = findPrefixRange(prefix);
        String start = range[0];
        String end = range[1];
        String identifier = UUID.randomUUID().toString();
        start += identifier;
        end += identifier;
        String key = "members:" + guild;
        conn.zadd(key, 0, start);
        conn.zadd(key, 0, end);
        Set<String> items;
        while (true) {
            conn.watch(key);
            int sindex = conn.zrank(key, start).intValue();
            int eindex = conn.zrank(key, end).intValue();
            int erange = Math.min(eindex - 2, sindex + 9);

            Transaction trans = conn.multi();
            trans.zrem(key, start);
            trans.zrem(key, end);
            trans.zrange(key, sindex, erange);
            List<Object> result = trans.exec();
            if (result != null) {
                items = (Set<String>) result.get(result.size() - 1);
                break;
            }
        }

        for (Iterator<String> iterator = items.iterator(); iterator.hasNext(); ) {
            if (iterator.next().contains("{")) {
                iterator.remove();
            }
        }
        return items;
    }


}
