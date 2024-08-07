/*
 * Copyright 2015-2024 Bloomreach (http://www.bloomreach.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onehippo.forge.oaipmh.provider.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import org.onehippo.forge.oaipmh.provider.model.oai.OAIPMHtype;
import org.onehippo.forge.oaipmh.provider.model.oai.dc.OaiDcType;
import org.onehippo.forge.oaipmh.provider.model.oai.edurep.lom.LomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Provider
public class JaxbContextProvider implements ContextResolver<JAXBContext> {

    private static final Logger log = LoggerFactory.getLogger(JaxbContextProvider.class);
    private JAXBContext context;
    private String beansPackage;
    private List<Class<?>> classes;

    public JaxbContextProvider() {
        log.debug("Creating custom JAXB ContextResolver");
    }

    @Override
    public JAXBContext getContext(Class<?> objectType) {
        if (context == null) {
            try {
                final List<Class<?>> allClasses = new ArrayList<>();

                includeAnnotatedTopLevelClassesFromBeansPackage(allClasses);
                if (classes != null) {
                    allClasses.addAll(classes);
                }
                allClasses.add(OAIPMHtype.class);
                allClasses.add(OaiDcType.class);
                allClasses.add(LomType.class);
                context = JAXBContext.newInstance(allClasses.toArray(new Class[allClasses.size()]));
            } catch (JAXBException | IOException | ClassNotFoundException e) {
                log.error("Error creating JAXB context:", e);
            }
        }
        return context;
    }

    private void includeAnnotatedTopLevelClassesFromBeansPackage(final List<Class<?>> allClasses)
            throws IOException, ClassNotFoundException {
        if (!Strings.isNullOrEmpty(beansPackage)) {
            final ClassPath classPath = ClassPath.from(getClass().getClassLoader());
            final ImmutableSet<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClassesRecursive(beansPackage);
            for (ClassPath.ClassInfo topLevelClass : topLevelClasses) {
                final String name = topLevelClass.getName();
                final Class<?> clazz = Class.forName(name);
                if (clazz.isAnnotationPresent(XmlRootElement.class)) {
                    log.debug("include {}", clazz);
                    allClasses.add(clazz);
                }
            }
        }
    }

    public List<Class<?>> getClasses() {
        return classes;
    }

    public void setClasses(final List<Class<?>> classes) {
        this.classes = classes;
    }

    public void setBeansPackage(String beansPackage) {
        this.beansPackage = beansPackage;
    }

    public String getBeansPackage() {
        return beansPackage;
    }
}
