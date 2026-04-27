
package com.example.mcstats;

public class StatsRecord {
    public String uuid;
    public int kills;
    public int deaths;
    public int playtime;

    public StatsRecord(String uuid, int kills, int deaths, int playtime) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.playtime = playtime;
    }
}
