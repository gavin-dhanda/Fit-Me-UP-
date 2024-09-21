import { expect, test } from "@playwright/test";
import { clearUser } from "../../src/utils/api";


const SPOOF_UID = "mock-user-id";

test.beforeEach(
  "add spoof uid cookie to browser",
  async ({ context, page }) => {
    await context.addCookies([
      {
        name: "uid",
        value: SPOOF_UID,
        url: "http://localhost:8000",
      },
    ]);

    // wipe everything for this spoofed UID in the database.
    await clearUser();
  }
);

test("on page load, I am automatically directed to the homepage", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/");
  await expect(page.getByLabel("Go to generate page")).toBeVisible();
  await expect(page.getByLabel("Go to saved page")).toBeVisible();
});

test("I can reload and stay on the same page", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/saved");
  await expect(page.getByText("Saved Outfits")).toBeVisible();
  page.reload();
  await expect(page.getByLabel("Go to saved page")).toBeVisible();
});

test("I can use the nav bar to navigate between pages", async ({ page }) => {
  await page.goto("http://localhost:8000/home");
  await page.getByLabel("Go to saved page").click();
  await expect(page.getByText("Saved Outfits")).toBeVisible();
  await page.getByLabel("Go to closet page").click();
  await expect(page.getByLabel("Show all clothes button")).toBeVisible();
});

test("I can view today's weather", async ({ page }) => {
  await page.goto("http://localhost:8000/home");
  await expect(page.getByLabel("Weather icon")).toBeVisible();
});

test("I can add an item", async ({ page }) => {
  await page.goto("http://localhost:8000/closet");
  await page.getByLabel("Add item").click();
});

test("I can filter clothing items", async ({ page }) => {
  await page.goto("http://localhost:8000/closet");
  await page.getByLabel("Show all clothes").click();
  await page.getByLabel("Show all tops").click();
  await page.getByLabel("Show all shoes").click();
});

test("I can't add an item if I don't fill out all fields", async ({ page }) => {
  await page.goto("http://localhost:8000/closet");
  await page.getByLabel("Add item").click();
  await page.getByLabel("submit item").click();
  await expect(page.getByText("Please fill out all fields!")).toBeVisible();
});

test("I can generate an outfit and save it", async ({ page }) => {
  await page.goto("http://localhost:8000/generate");
  await page.getByLabel("Generate button").click();
  await page.getByLabel("Save button").click();
});

test("I can toggle between formal, informal, and flex outfit generation", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/generate");
  await expect(page.getByText("Informal")).toBeVisible();
  await page.getByLabel("Type button").click();
  await expect(page.getByText("Formal")).toBeVisible();
  await page.getByLabel("Type button").click();
  await expect(page.getByText("Flex")).toBeVisible();
  await page.getByLabel("Type button").click();
  await expect(page.getByText("Informal")).toBeVisible();
});
