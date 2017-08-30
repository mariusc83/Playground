import okhttp3.Headers
import org.jetbrains.spek.api.Spek
import org.mariusc.gitdemo.data.network.repository.parse.UrlLinkHeadersParserUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class UrlLinkHeadersParserUtilsTest : Spek({
    on("malformed url") {
        val lastUrl = "https://api.github.com/repositories?since=1479";
        val firstUrl = "https://api.github.com/repositories{?since}";
        val header = "Link:$lastUrl>; rel=\"last\", <$firstUrl>; rel=\"first\"";
        val mockHeaders = Headers.Builder().add(header).build();

        it("will return only the firstUrl in the header") {
            // when
            val parsedHeaders = UrlLinkHeadersParserUtils.parse(mockHeaders);


            // then
            assertThat(parsedHeaders.size).isEqualTo(1);
            assertThat(parsedHeaders.get(UrlLinkHeadersParserUtils.FIRST))
                    .isEqualTo(firstUrl);

        }
    }

    on("malformed url relation") {
        val lastUrl = "https://api.github.com/repositories?since=1479";
        val firstUrl = "https://api.github.com/repositories{?since}";
        val header = "Link:$lastUrl>; rel=\"last\", <$firstUrl>; rel=\"first\"";
        val mockHeaders = Headers.Builder().add(header).build();

        it("will return only the firstUrl in the header") {
            // when
            val parsedHeaders = UrlLinkHeadersParserUtils.parse(mockHeaders);


            // then
            assertThat(parsedHeaders.size).isEqualTo(1);
            assertThat(parsedHeaders.get(UrlLinkHeadersParserUtils.FIRST))
                    .isEqualTo(firstUrl);

        }
    }

    on("first page header") {
        val nextUrl = "https://api.github.com/repositories?since=1479"
        val firstUrl = "https://api.github.com/repositories{?since}"
        val header = "Link:<$nextUrl>; rel=\"next\", <$firstUrl>; rel=\"first\"";

        val mockHeaders = Headers.Builder().add(header).build()


        it("will return valid parsed headers") {
            val parsedHeaders: Map<String, String> = UrlLinkHeadersParserUtils.parse(mockHeaders);


            assertThat(parsedHeaders.size).isEqualTo(2);
            val next = parsedHeaders.get(UrlLinkHeadersParserUtils.NEXT);
            val first = parsedHeaders.get(UrlLinkHeadersParserUtils.FIRST);
            assertThat(first).isEqualTo(firstUrl);
            assertThat(next).isEqualTo(nextUrl);
        }
    }

    on("before last page header") {
        val lastUrl = "https://api.github.com/repositories?since=1479";
        val firstUrl = "https://api.github.com/repositories{?since}";
        val header = "Link:<$lastUrl>; rel=\"last\", <$firstUrl>; rel=\"first\"";
        val mockHeaders = Headers.Builder().add(header).build();

        it("will return valid parsed headers") {
            // when
            val parsedHeaders = UrlLinkHeadersParserUtils.parse(mockHeaders);


            // then
            assertThat(parsedHeaders.size).isEqualTo(2);
            assertThat(parsedHeaders.get(UrlLinkHeadersParserUtils.FIRST)).isEqualTo(firstUrl);
            assertThat(parsedHeaders.get(UrlLinkHeadersParserUtils.LAST)).isEqualTo(lastUrl);

        }
    }

    on("empty page header") {
        val header = "Link:"
        val mockHeaders = Headers.Builder().add(header).build()


        it("will return empty map") {
            // when
            val parsedHeaders = UrlLinkHeadersParserUtils.parse(mockHeaders)

            // then
            assertThat(parsedHeaders).isEmpty()
        }
    }

    on("valid since url") {
        val since = "1479"
        val url = "https://api.github.com/repositories?since=$since"

        it("will return the valid 'since' value") {
            // when
            val parsedSince = UrlLinkHeadersParserUtils.sinceFromUrl(url)
            // then
            assertThat(parsedSince).isEqualTo(since)
        }
    }

    on("valid first page url") {
        val url = "https://api.github.com/repositories{?since}"

        it("will empty 'since' value") {
            // when
            val parsedSince = UrlLinkHeadersParserUtils.sinceFromUrl(url)
            // then
            assertThat(parsedSince).isEmpty()
        }
    }
})
