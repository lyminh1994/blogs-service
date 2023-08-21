import requests
from bs4 import BeautifulSoup as bs


class Article:
    def __init__(self):
        self.title = None
        self.slug = None
        self.description = None
        self.tags = None

    def set_article(self, title, slug, description, tags):
        self.title = title
        self.slug = slug
        self.description = description
        self.tags = tags


def read_card(page_url):
    cards_page = requests.get(page_url)
    cards_soup = bs(cards_page.content, HTML_PARSER)
    cards = cards_soup.find_all("div", class_="card")
    for card in cards:
        banner = card.find("img", class_="img-fluid")
        card_body = card.find("div", class_="card-body")
        header = card_body.find("h3", class_="h6")
        card_meta = card_body.find("ul", class_="card-meta")
        avatar = card_meta.find("img", class_="card-avatar")
        author_name = card_meta.find("a")
        create_date = card_meta.find("span")
        card_text = card_body.find("p", class_="card-text")
        card_badge = card.find("ul", class_="card-badge")
        badges = card_badge.find_all("a", class_="badge")
        badges_text = []
        for badge in badges:
            badges_text.append(badge.text.strip())
        print(banner)
        print(header.text.strip())
        print(avatar)
        print(author_name.text.strip())
        print(create_date.text.strip())
        print(card_text.text.strip())
        print(badges_text)


HTML_PARSER = "html.parser"

URL = "https://reflectoring.io/"
# find all categories
categories_url = f"{URL}categories/"
categories_page = requests.get(categories_url)
categories_soup = bs(categories_page.content, HTML_PARSER)

section = categories_soup.find("section", class_="section")
categories = section.find("div", class_="container")
for a in categories.find_all("a", href=True):
    page = requests.get(f"{URL}{a['href']}")
    soup = bs(page.content, HTML_PARSER)
    next_link = soup.find("li", class_="next")
    if next_link is None:
        read_card(f"{URL}{a['href']}")
