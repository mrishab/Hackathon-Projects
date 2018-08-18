package com.sap.orca.starter.sample.jaxrs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sap.orca.starter.core.util.DbUtils;
import com.sap.orca.starter.data.ds.DataSourcePool;

@Component
@Path("/sql")
public class Sql {

	@Autowired
	private DataSourcePool dsPool;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Sql.class);
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON}) 
	@Produces({MediaType.APPLICATION_JSON})
	public Response getData(String body) {
		JsonParser parser = new JsonParser();
		JsonObject jsonBody = parser.parse(body).getAsJsonObject();
		String drugColumns = "(name, id, reportId)";
		String reportID = UUID.randomUUID().toString();
		String columns = "(id, ";
		String values = "(\'" + reportID + "\', ";
		List<String> drugValues = new ArrayList<String>();
		Set<Map.Entry<String, JsonElement>>entrySet = jsonBody.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			LOGGER.error("key: " + entry.getKey() + " value: " + entry.getValue());
			if (entry.getKey().equals("drugs_given")) {
				JsonArray drugs = entry.getValue().getAsJsonArray();
				drugValues = drugUpdate(drugs, reportID);
			} else {
				columns += entry.getKey() + ", ";

				JsonPrimitive value = entry.getValue().getAsJsonPrimitive();
				if (value.isString()) {
					if (entry.getKey().equals("date")) {
						values += "TO_DATE(\'" + entry.getValue().getAsString() + "\', \'DD/MM/YYYY\'), ";
					} else {
						values += "\'" + entry.getValue().getAsString() + "\', ";
					}
				} else if (value.isNumber()) {
					values += entry.getValue().getAsInt() + ", ";
				}
			}
		}
		columns = columns.substring(0, columns.length() - 2) + ")";
		values = values.substring(0, values.length() - 2) + ")";
		String tableName = "Report"; 
		String sql = "INSERT INTO " + tableName + " " + columns + " VALUES " + values;
		DataSource dataSource = dsPool.getDataSource();
		Connection connection;
		JsonObject response = new JsonObject();
		try {
			connection = dataSource.getConnection();
			LOGGER.error("main query: " + sql);
			executeStatement(connection,sql);
			for(String drug : drugValues) {
				String drugSql = "INSERT INTO Drug " + drugColumns + " VALUES " + drug;
				LOGGER.error("drug query: " + drugSql);
				connection = dataSource.getConnection();
				executeStatement(connection, drugSql);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("status", "Failed");
			response.addProperty("error", e.getMessage());
			return Response.serverError().entity(response.toString()).build();
		}
		response.addProperty("status", "Success");
		return Response.ok(response.toString()).build();
	}
	
	private List<String>drugUpdate(JsonArray drugs, String reportID ) {
		List<String> drugValues = new ArrayList<String>();
		for(JsonElement drug : drugs) {
			String drugID = UUID.randomUUID().toString();
			String values = "(\'";
			values += drug.getAsString() + "\', \'" + drugID + "\', \'" + reportID + "\')";
			drugValues.add(values);
		}
			return drugValues;
	}
	
	private Object executeStatement(Connection connection, String sqlQuery) throws SQLException {
        Object result = null;
        	try {
            result = DbUtils.update(connection, sqlQuery);
        	} catch (SQLException e) {
        		throw new SQLException(e);
        	}
        	finally {
            DbUtils.commitAndClose(connection);
        	}
        return result;
    }
	
	
}
