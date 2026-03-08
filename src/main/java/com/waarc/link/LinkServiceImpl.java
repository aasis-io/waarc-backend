package com.waarc.link;

import com.google.gson.Gson;

import com.waarc.exception.ResourceNotFoundException;
import io.javalin.http.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository = new LinkRepositoryImpl();
    private final Logger LOG = LogManager.getLogger(LinkServiceImpl.class);

    @Override
    public String getLink(Context ctx) {
        ctx.contentType("application/json");
        try {
            List<Link> link = linkRepository.getlink();

            ctx.status(200);
            return new Gson().toJson(link);

        } catch (ResourceNotFoundException e) {
            ctx.status(404);
            return "link not Found";
        } catch (Exception e) {
            ctx.status(500);
            return e.getMessage();
        }
    }

    @Override
    public String save(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Creating link!");
        try {
            LinkRequest request = ctx.bodyAsClass(LinkRequest.class);
            return new Gson().toJson(linkRepository.save(request));
        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error creating link: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String updateLink(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Updating link!");
        try {
            if(ctx.queryParam("id") == null) {
                ctx.contentType("application/json");
                ctx.status(400);
                return "Link not Found";
            }
            Link existinglink = linkRepository.getlinkById(ctx.queryParam("id"));

            LinkRequest request = ctx.bodyAsClass(LinkRequest.class);

            LOG.info("Updating existing link...");
            return new Gson().toJson(linkRepository.updatelink(request, existinglink.getId()));

        } catch (ResourceNotFoundException e) {
            LOG.info("Link not found!");
            ctx.status(404);
            return "Link not Found";
        }catch (Exception e) {
            ctx.status(500);
            LOG.error("Error upserting link: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String getIndividualLink(Context ctx) {

        try {
            if(ctx.queryParam("id") == null) {
                ctx.contentType("application/json");
                ctx.status(400);
                return "Link not Found";
            }
            Link existinglink = linkRepository.getlinkById(ctx.queryParam("id"));
            return new Gson().toJson(existinglink);
        }catch (ResourceNotFoundException e) {
            ctx.status(404);
            return "link not Found";
        }catch (Exception e) {
            ctx.status(500);
            LOG.error("Error getting link: " + e.getMessage(), e);
            return "Error : " + e.getMessage();
        }


    }

    @Override
    public String deleteLink(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Deleting link!");
        try {
            if(ctx.queryParam("id") == null) {
            ctx.contentType("application/json");
            ctx.status(400);
            return "Link not Found";
            }
            Link existinglink = linkRepository.getlinkById(ctx.queryParam("id"));

            String response = linkRepository.deletelinkById(existinglink.getId());
            return new Gson().toJson(response);
        }catch ( ResourceNotFoundException e) {
            ctx.status(404);
            return "Link not Found";
        } catch ( Exception e){
            ctx.status(500);
            LOG.error("Error deleting link: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    /**
     * Helper method to parse form fields, file, and JSON whyUs array
     */
    private LinkRequest parseFormData(Context ctx) throws Exception {
        String title = ctx.formParam("title");
        String link = ctx.formParam("link");


        Map<String, String> fields = new HashMap<>();
        fields.put("title", title);
        fields.put("link", link);


        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                throw new IllegalArgumentException("Bad Request: " + entry.getKey() + " is required");
            }
        }

        LinkRequest request = new LinkRequest();
        request.setTitle(title);
        request.setLink(link);


        return request;
    }
}