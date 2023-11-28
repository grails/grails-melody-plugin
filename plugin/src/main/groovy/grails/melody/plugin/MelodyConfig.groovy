package grails.melody.plugin

import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import groovy.util.logging.Slf4j
import net.bull.javamelody.MonitoringFilter
import net.bull.javamelody.SessionListener
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean

import javax.servlet.DispatcherType
import javax.servlet.ServletContext
import javax.servlet.ServletException

/**
 * Class to initialize Melody Filter
 */
@Slf4j
class MelodyConfig implements GrailsApplicationAware {

    GrailsApplication grailsApplication

    @Bean
    ServletContextInitializer melodyInitializer() {
        return new ServletContextInitializer() {
            @Override
            void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.addListener(new SessionListener())
            }
        }
    }

    @Bean
    FilterRegistrationBean melodyFilter() {
        log.debug "Creating Melody Filter..."
        FilterRegistrationBean melodyFilterBean = new FilterRegistrationBean()
        MonitoringFilter melodyFilter = new MonitoringFilter()
        melodyFilter.setApplicationType("Grails")
        melodyFilterBean.setFilter(melodyFilter)
        melodyFilterBean.setAsyncSupported(true)
        melodyFilterBean.setName(MonitoringFilter.name)
        melodyFilterBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC)
        def conf = GrailsMelodyUtil.getGrailsMelodyConfig(grailsApplication)?.javamelody
        conf?.each {
            String name = it.key
            String value = it.value
            log.debug "Grails Melody Param: $name = $value"

            melodyFilterBean.addInitParameter(name, value)
        }

        melodyFilterBean.addUrlPatterns("/*")

        melodyFilterBean
    }

}
