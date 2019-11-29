package org.cayambe;

import org.cayambe.model.Category;
import org.cayambe.model.CategoryTree;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;
import java.util.List;


@Path("")
@ApplicationScoped
public class CategoryResource {

    @PersistenceContext(unitName = "AdminPU")
    private EntityManager em;

    @GET
    @Path("/categorytree")
    @Produces(MediaType.APPLICATION_JSON)
    public CategoryTree tree() throws Exception {
        return em.find(CategoryTree.class, 1);
    }

    @GET
    @Path("/category/{category-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category get(@PathParam("category-id") Integer categoryId) throws Exception {
        return em.find(Category.class, categoryId);
    }

    @GET
    @Path("/category")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Category> getAll() throws Exception {
        return em.createNamedQuery("Category.findAll", Category.class).getResultList();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/{category-id}")
    @Transactional
    public Response remove(@PathParam("category-id") Integer categoryId) throws Exception {

        try {
            Category entity = em.find(Category.class, categoryId);
            em.remove(entity);
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Category category) throws Exception {
        if (category.getId() != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Unable to create Category, id was already set.")
                    .build();
        }

        Category parent;
        if((parent = category.getParent()) != null && parent.getId() != null) {
            category.setParent(get(parent.getId()));
        }

        try {
            em.persist(category);
            em.flush();
        }
        catch (ConstraintViolationException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.created(new URI("category/" + category.getId().toString())).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/{categoryId}")
    @Transactional
    public Response update(@PathParam("categoryId") Integer categoryId, Category category) throws Exception {
        try {
            Category entity = em.find(Category.class, categoryId);

            if (null == entity) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("Category with id of " + categoryId + " does not exist.")
                        .build();
            }

            em.merge(category);

            return Response
                    .ok(category)
                    .build();
        } catch (Exception e) {
            return Response
                    .serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }
}
