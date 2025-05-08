package com.chesire.nekomp.library.datasource.favorite.remote.model

import com.chesire.nekomp.library.datasource.kitsumodels.ImagesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrieveFavoriteCharacterResponseDto(
    @SerialName("data")
    val data: List<DataDto>,
    @SerialName("included")
    val included: List<IncludedDto>?
)

@Serializable
data class DataDto(
    @SerialName("id")
    val id: Int,
    @SerialName("attributes")
    val attributes: Attributes,
    @SerialName("relationships")
    val relationships: Relationships
) {

    @Serializable
    data class Attributes(
        @SerialName("favRank")
        val favRank: Int
    )

    @Serializable
    data class Relationships(
        @SerialName("item")
        val item: RelationshipObject? = null
    ) {

        @Serializable
        data class RelationshipObject(
            @SerialName("data")
            val data: RelationshipData? = null
        ) {

            @Serializable
            data class RelationshipData(
                @SerialName("id")
                val id: Int
            )
        }
    }
}

@Serializable
data class IncludedDto(
    @SerialName("id")
    val id: Int,
    @SerialName("attributes")
    val attributes: Attributes
) {

    @Serializable
    data class Attributes(
        @SerialName("canonicalName")
        val canonicalName: String,
        @SerialName("image")
        val image: ImagesDto?
    )
}


/*
{
  "data" : [ {
    "id" : "1753919",
    "type" : "favorites",
    "links" : {
      "self" : "https://kitsu.io/api/edge/favorites/1753919"
    },
    "attributes" : {
      "createdAt" : "2025-05-07T21:04:32.999Z",
      "updatedAt" : "2025-05-07T21:04:32.999Z",
      "favRank" : 0
    },
    "relationships" : {
      "user" : {
        "links" : {
          "self" : "https://kitsu.io/api/edge/favorites/1753919/relationships/user",
          "related" : "https://kitsu.io/api/edge/favorites/1753919/user"
        }
      },
      "item" : {
        "links" : {
          "self" : "https://kitsu.io/api/edge/favorites/1753919/relationships/item",
          "related" : "https://kitsu.io/api/edge/favorites/1753919/item"
        },
        "data" : {
          "type" : "characters",
          "id" : "38956"
        }
      }
    }
  } ],
  "included" : [ {
    "id" : "38956",
    "type" : "characters",
    "links" : {
      "self" : "https://kitsu.io/api/edge/characters/38956"
    },
    "attributes" : {
      "createdAt" : "2013-05-19T21:46:17.932Z",
      "updatedAt" : "2021-09-26T11:46:17.698Z",
      "slug" : "rachel-alucard",
      "names" : {
        "en" : "Rachel Alucard",
        "ja_jp" : "レイチェル＝アルカード"
      },
      "canonicalName" : "Rachel Alucard",
      "otherNames" : [ "Rabbit", "Bunny-Leech", "Harlequin" ],
      "name" : "Rachel Alucard",
      "malId" : 33804,
      "description" : "Age: Immortal (biologically her body is that of a 12 to 14 year old girl) Race: Vampire Birthday: October 31 Birthplace: Transylvania Height: 145 cm Weight: 31 kg Blood type: Unknown Hobby: teatime Values: time, her Meissen teacup Likes: sweet things (especially cake) Dislikes: being bored, bell peppers, tomato juice  Rachel Alucard is the current head of the Alucard family, and a very powerful being. Rachel behaves like a stereotypical upper-class individual, often looking down upon others with boredom, arrogance, snobbery and apathy, and hates her vampire urges.  She is seemingly one of the most powerful characters in the series to the point where she can 'keep up' with Terumi. Rachel can create a dimension called the Requiem which intersects with her dream world. She is always with her servants Nago and Gii and usually with her butler, Valkenhayn Hellsing. Both she and Valkenhayn seem to be the only characters who know that time was looping the Calamity Trigger a century before. She plays with the characters to push them forward to breaking the loop of time that is repeating itself. She has the Tsukuyomi Unit, a powerful Sankishin Unit which has, according to Rachel, an \"absolute defense\". The \"absolute defense\" takes the form of a golden shield which defends Kagutsuchi from a laser fired by the Nox Nyctores known as Take-Mikazuchi.  In BlazBlue: Calamity Trigger, an extremely bored Rachel spent much of her time trying to secretly direct characters from making the same mistakes over and over again and break the looping of time, but as she was an Observer, she could do nothing but stand in the sidelines.  She has many hair ties, so people call her rabbit which irritates her, although Ragna seems to get away with calling her rabbit, idiot, and 'bunny-leech' in the second game. The only concern whom she seems to show towards is Ragna. It is also further insinuated that Rachel deeply cares for Ragna as its shown during Ragna's bad ending. Interestingly enough, in some later interactions it is revealed that she is possibly the most powerful of the playable characters, and as such the only one capable of taking on Terumi. In battle she has mastered sorcery and has the ability to control lightning and wind.  Rachel is a stereotypical aristocratic heiress. She has an almost enchanting air of dignity and grace, yet is sarcastic and condescending to those she considers lower than her, always expecting them to have the highest standards of formality when conversing with her.  Despite this, she does care deeply for her allies. Her butler, Valkenhayn, is fervently devoted to Rachel, as he was a loyal friend and respected rival to her father, the late Clavis Alucard. Rachel's familiars, Nago and Gii, despite taking punishment from her on a regular basis, remain loyal to her.  Perhaps her most intriguing relationship is with Ragna. Although Rachel would never admit to it, she loves Ragna for his determination and unwillingness to give up even when the odds are against him.",
      "image" : {
        "tiny" : "https://media.kitsu.app/character/38956/image/tiny-b7583cc444d265ed95e6ddb1d3326702.jpeg",
        "large" : "https://media.kitsu.app/character/38956/image/large-ddbd7d7260eb20a2c4381de74d388810.jpeg",
        "small" : "https://media.kitsu.app/character/38956/image/small-54d05d8031c76805da8c2afc24be3a8c.jpeg",
        "medium" : "https://media.kitsu.app/character/38956/image/medium-56ea46148521c8213a8acf4340621946.jpeg",
        "original" : "https://media.kitsu.app/characters/images/38956/original.jpg",
        "meta" : {
          "dimensions" : {
            "tiny" : {
              "width" : 100,
              "height" : 120
            },
            "large" : {
              "width" : 500,
              "height" : 600
            },
            "small" : {
              "width" : 200,
              "height" : 240
            },
            "medium" : {
              "width" : 300,
              "height" : 360
            }
          }
        }
      }
    },
    "relationships" : {
      "primaryMedia" : {
        "links" : {
          "self" : "https://kitsu.io/api/edge/characters/38956/relationships/primary-media",
          "related" : "https://kitsu.io/api/edge/characters/38956/primary-media"
        }
      },
      "castings" : {
        "links" : {
          "self" : "https://kitsu.io/api/edge/characters/38956/relationships/castings",
          "related" : "https://kitsu.io/api/edge/characters/38956/castings"
        }
      },
      "mediaCharacters" : {
        "links" : {
          "self" : "https://kitsu.io/api/edge/characters/38956/relationships/media-characters",
          "related" : "https://kitsu.io/api/edge/characters/38956/media-characters"
        }
      },
      "quotes" : {
        "links" : {
          "self" : "https://kitsu.io/api/edge/characters/38956/relationships/quotes",
          "related" : "https://kitsu.io/api/edge/characters/38956/quotes"
        }
      }
    }
  } ],
  "meta" : {
    "count" : 1
  },
  "links" : {
    "first" : "https://kitsu.io/api/edge/favorites?filter%5BitemType%5D=Character&filter%5BuserId%5D=294558&include=item&page%5Blimit%5D=10&page%5Boffset%5D=0",
    "last" : "https://kitsu.io/api/edge/favorites?filter%5BitemType%5D=Character&filter%5BuserId%5D=294558&include=item&page%5Blimit%5D=10&page%5Boffset%5D=0"
  }
}
 */
