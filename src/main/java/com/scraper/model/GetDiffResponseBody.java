package com.scraper.model;

import java.util.List;

import lombok.Data;

@Data
public class GetDiffResponseBody {
private List<PlanetPosition> today;
private List<PlanetPosition> yesterday;
private List<DegreeDiff> diff;
}
